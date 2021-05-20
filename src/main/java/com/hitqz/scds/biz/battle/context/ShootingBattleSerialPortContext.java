package com.hitqz.scds.biz.battle.context;

import cn.hutool.extra.spring.SpringUtil;
import com.hitqz.scds.biz.battle.domain.*;
import com.hitqz.scds.biz.battle.service.HandleService;
import com.hitqz.scds.biz.battle.thread.*;
import com.hitqz.scds.biz.map.domain.MapEntity;
import com.hitqz.scds.biz.shootingvest.domian.ShootingVestModel;
import com.hitqz.scds.common.Constant;
import com.hitqz.scds.utils.DataFrameUtils;
import jssc.SerialPort;
import jssc.SerialPortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

public class ShootingBattleSerialPortContext implements ShootingBattleContext {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    // 需要外部传入的属性
    private BattleEntity battleEntity;
    private BattleBaseConfigEntity battleBaseConfigEntity;
    private List<BattleTeamConfigEntity> battleTeamConfigEntityList;
    private MapEntity mapEntity;
    private SerialPort serialPort;
    private SerialPort virtualSerialPort;

    // 需要内部自己初始化的属性
    private boolean isStop;
    private Map<Integer, ShootingVestModel> vestEntityMap;
    private BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue;
    private HandleService handleService;
    private ExecutorService executor;
    private SerialPortWriteDataThread serialPortWriteDataThread;
    private InstructionProcessThread instructionProcessThread;
    private IndoorLocationThread indoorLocationThread;
    private MapDataThread mapDataThread;
    private BattleRecordThread battleRecordThread;
    private ScheduledExecutorService scheduledExecutorService;
    private BattleRollThread battleRollThread;

    public ShootingBattleSerialPortContext(BattleEntity battleEntity
            , BattleBaseConfigEntity battleBaseConfigEntity
            , List<BattleTeamConfigEntity> battleTeamConfigEntityList, MapEntity mapEntity
            , SerialPort serialPort, SerialPort virtualSerialPort) {
        this.battleEntity = battleEntity;
        this.battleBaseConfigEntity = battleBaseConfigEntity;
        this.battleTeamConfigEntityList = battleTeamConfigEntityList;
        this.mapEntity = mapEntity;
        this.serialPort = serialPort;
        this.virtualSerialPort = virtualSerialPort;
    }

    @Override
    public void init() throws SerialPortException, IOException {
        // 初始化马甲信息列表
        vestEntityMap = new ConcurrentHashMap<>();
        for (BattleTeamConfigEntity battleTeamConfigEntity : battleTeamConfigEntityList) {
            for (int num = battleTeamConfigEntity.getStartNum(); num <= battleTeamConfigEntity.getEndNum(); num++) {
                ShootingVestModel model = ShootingVestModel.ShootingVestModelBuilder
                        .aShootingVestModel()
                        .name("")
                        .team(battleTeamConfigEntity.getTeam())
                        .num(num)
                        .hp(100)
                        .weapon1(battleTeamConfigEntity.getPrimaryWeapon())
                        .ammo1(0)
                        .weapon2(battleTeamConfigEntity.getSecondaryWeapon())
                        .ammo2(0)
                        .lat(BigDecimal.ZERO)
                        .lng(BigDecimal.ZERO)
                        .mode(battleBaseConfigEntity.getMode())
                        .status(Constant.OFF_LINE_STATUS)
                        .build();
                vestEntityMap.put(num, model);
            }
        }

        // 初始化任务队列
        rollingMissionBlockingQueue = new PriorityBlockingQueue<>(256, new SendDataMissionModelComparator());

        // 注册串口监听事件
        serialPort.addEventListener(serialPortEvent -> readData());
        if (Constant.INDOOR_TYPE == mapEntity.getType()) {
            virtualSerialPort.addEventListener(serialPortEvent -> readIndoorData());
        }

        // 初始化线程池
        executor =  Executors.newFixedThreadPool(Constant.INDOOR_TYPE == mapEntity.getType() ? 5 : 3);
        scheduledExecutorService = Executors.newScheduledThreadPool(1);

        // 初始化轮询线程
        battleRollThread = new BattleRollThread(vestEntityMap.values(), rollingMissionBlockingQueue);

        // 初始化数据处理线程
        handleService = SpringUtil.getBean(HandleService.class);
        serialPortWriteDataThread = new SerialPortWriteDataThread(this, serialPort, rollingMissionBlockingQueue);
        instructionProcessThread = new InstructionProcessThread(this, new LinkedBlockingDeque<>(1024), handleService);
        if (Constant.INDOOR_TYPE == mapEntity.getType()) {
            mapDataThread = new MapDataThread(this, mapEntity, vestEntityMap.values(), rollingMissionBlockingQueue);
            indoorLocationThread = new IndoorLocationThread(this, new LinkedBlockingDeque<>(1024), handleService);
        }
        battleRecordThread = new BattleRecordThread(this, battleEntity.getId(), vestEntityMap.values());
    }

    @Override
    public void start() throws InterruptedException {
        isStop = false;
        // 下发演习开始指令
        SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                .aSendDataMissionModel()
                .vestNum(0)
                .isWait(false)
                .data(DataFrameUtils.createStartBattleData(battleBaseConfigEntity.getMode()))
                .build();
        rollingMissionBlockingQueue.put(data);

        // 1秒执行一次轮询
        scheduledExecutorService.scheduleAtFixedRate(battleRollThread, 1, 1, TimeUnit.SECONDS);

        // 启动各个数据处理线程
        executor.execute(serialPortWriteDataThread);
        executor.execute(instructionProcessThread);
        if (Constant.INDOOR_TYPE == mapEntity.getType()) {
            executor.execute(mapDataThread);
            executor.execute(indoorLocationThread);
        }
        executor.execute(battleRecordThread);
    }

    @Override
    public void stop() throws SerialPortException, InterruptedException {
        isStop = true;
        SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                .aSendDataMissionModel()
                .vestNum(0)
                .isWait(false)
                .data(DataFrameUtils.createFinishBattleData())
                .build();
        for (int i = 0; i <= 4; i++){
            rollingMissionBlockingQueue.put(data);
        }

        TimeUnit.SECONDS.sleep(2);

        scheduledExecutorService.shutdown();
        executor.shutdownNow();

        while (!executor.isTerminated()) {
            log.info("[演习管理]：当前工作线程还未全部关闭，等待全部数据处理线程关闭……");
            TimeUnit.SECONDS.sleep(1);
        }

        serialPort.removeEventListener();
        if (Constant.INDOOR_TYPE == mapEntity.getType()) {
            virtualSerialPort.removeEventListener();
        }

        vestEntityMap.clear();
        vestEntityMap = null;
        rollingMissionBlockingQueue.clear();
        log.info("[演习管理]：当前工作线程全部关闭，释放所有资源");
    }

    private void readData() {
        byte[] data = receiveData(serialPort, "串口管理");
        if (Objects.nonNull(data)) {
            try {
                instructionProcessThread.getDataQueue().put(data);
            } catch (InterruptedException e) {
                log.error("[指令处理]：发生异常", e);
            }
        }
    }

    private void readIndoorData() {
        byte[] data = receiveData(virtualSerialPort, "虚拟串口管理");
        if (Objects.nonNull(data)) {
            try {
                indoorLocationThread.getDataQueue().put(data);
            } catch (InterruptedException e) {
                log.error("[指令处理]：发生异常", e);
            }
        }
    }

    private byte[] receiveData(SerialPort serialPort, String serialPortTips) {
        byte[] bytes = null;
        try {
            bytes = serialPort.readBytes();
            if (Objects.nonNull(bytes)) {
                log.info("[{}]：收到的数据长度[{}]", serialPortTips, bytes.length);
                log.info("[{}]：收到的数据[{}]", serialPortTips, DatatypeConverter.printHexBinary(bytes));
                if (bytes[0] == 1) {
                    log.info("[{}]：设备识别码为0x01的数据,暂停轮询线程", serialPortTips);
                }
            }
        } catch (SerialPortException e) {
            log.error("[{}]：串口通讯发生错误[{}]", serialPortTips, e.getExceptionType(), e);
        }
        return bytes;
    }

    @Override
    public Map<Integer, ShootingVestModel> getVestEntityMap() {
        return vestEntityMap;
    }

    @Override
    public BattleEntity getBattleEntity() {
        return this.battleEntity;
    }

    @Override
    public MapDataThread getMapDataThread() {
        return this.mapDataThread;
    }

    @Override
    public BattleRecordThread getBattleRecordThread() {
        return this.battleRecordThread;
    }

    @Override
    public MapEntity getMapEntity() {
        return this.mapEntity;
    }

    @Override
    public BlockingQueue<SendDataMissionModel> getRollingMissionBlockingQueue() {
        return this.rollingMissionBlockingQueue;
    }

    @Override
    public boolean isStop() {
        return this.isStop;
    }
}
