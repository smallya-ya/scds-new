package com.hitqz.scds.biz.license.service;

import cn.hutool.system.oshi.OshiUtil;
import com.hitqz.scds.biz.license.domain.LicenseEntity;
import com.hitqz.scds.biz.license.domain.RegisterRequestDO;
import com.hitqz.scds.biz.license.mapper.LicenseEntityMapper;
import com.hitqz.scds.common.Constant;
import com.hitqz.scds.common.domain.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@DependsOn("flywayConfig")
@Service
public class LicenseService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private String machineId;
    private String key;
    private boolean isRegistered = false;
    private static final String KEY_ID = "outsider";
    private LicenseEntity licenseEntity;

    @Autowired
    private LicenseEntityMapper licenseEntityMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${serverUrl}")
    public String serverUrl;

    @Value("${singleMode}")
    public int singleMode;

    @PostConstruct
    public void init() {
        if (Constant.ONLINE_MODE == singleMode) {
            log.info("[license服务]：联机模式");
            this.machineId = OshiUtil.getProcessor().getProcessorIdentifier().getProcessorID();
            log.info("[license服务]：本机ID为{}", this.machineId);
            LicenseEntity licenseEntity = licenseEntityMapper.selectById(KEY_ID);
            if (Objects.nonNull(licenseEntity)){
                String md5 = DigestUtils.md5DigestAsHex((KEY_ID + this.machineId).getBytes());
                if (md5.equals(licenseEntity.getKey())){
                    isRegistered = true;
                    key = md5;
                    this.licenseEntity = licenseEntity;
                    log.info("[license服务]：本机已注册");
                }
            }
        } else {
            log.info("[license服务]：单机模式");
            this.machineId = OshiUtil.getProcessor().getProcessorIdentifier().getProcessorID();
            log.info("[license服务]：本机ID为{}", this.machineId);
            this.isRegistered = true;
            LicenseEntity licenseEntity = licenseEntityMapper.selectById(KEY_ID);
            if (Objects.isNull(licenseEntity)){
                licenseEntity = new LicenseEntity();
                licenseEntity.setId(KEY_ID);
                licenseEntity.setMachineName("管理员");
            }
            this.licenseEntity = licenseEntity;
        }
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    @Transactional
    public BaseResponse register(RegisterRequestDO registerRequest){
        log.info("[license服务]：{} {} {} 进行注册", registerRequest.getUserName(), registerRequest.getPassword(), registerRequest.getMachineName());
        registerRequest.setMachineId(machineId);
        try {
            BaseResponse baseResponse = restTemplate.postForEntity(serverUrl + "/client/register", registerRequest, BaseResponse.class).getBody();
            if (baseResponse.getCode() == HttpServletResponse.SC_OK){
                isRegistered = true;
                licenseEntityMapper.deleteById(KEY_ID);
                LicenseEntity licenseEntity = new LicenseEntity();
                licenseEntity.setId(KEY_ID);
                licenseEntity.setKey((String) baseResponse.getData());
                licenseEntity.setMachineName(registerRequest.getMachineName());
                licenseEntityMapper.insert(licenseEntity);
                this.licenseEntity = licenseEntity;
            }
            return baseResponse;
        } catch (RestClientException e){
            return BaseResponse.BaseResponseBuilder
                    .aBaseResponse()
                    .code(HttpServletResponse.SC_BAD_GATEWAY)
                    .msg("无法连接至服务器，请检查本机网络是否联网。")
                    .build();
        } catch (Exception e) {
            return BaseResponse.BaseResponseBuilder
                    .aBaseResponse()
                    .code(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .msg("[license服务]：注册发生异常")
                    .build();
        }
    }

    public String getMachineName(){
        return Objects.isNull(this.licenseEntity) ? "" : this.licenseEntity.getMachineName();
    }
}
