package com.cetc.cctv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.cetc.cctv.domain.enumeration.DeviceStatus;

import com.cetc.cctv.domain.enumeration.FilterType;

/**
 * 摄像头
 */
@ApiModel(description = "摄像头")
@Entity
@Table(name = "camera")
@Document(indexName = "camera")
public class Camera implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 摄像头名称
     */
    @ApiModelProperty(value = "摄像头名称")
    @Column(name = "name")
    private String name;

    /**
     * 摄像头ID
     */
    @ApiModelProperty(value = "摄像头ID")
    @Column(name = "camera_id")
    private String cameraID;

    /**
     * 摄像头IP
     */
    @ApiModelProperty(value = "摄像头IP")
    @Column(name = "camera_ip")
    private String cameraIP;

    /**
     * 所属服务器
     */
    @ApiModelProperty(value = "所属服务器")
    @Column(name = "belong_server")
    private String belongServer;

    /**
     * 所属通道
     */
    @ApiModelProperty(value = "所属通道")
    @Column(name = "belong_channel")
    private String belongChannel;

    /**
     * 采集标准
     */
    @ApiModelProperty(value = "采集标准")
    @Column(name = "collect_standards")
    private String collectStandards;

    /**
     * 设备状态
     */
    @ApiModelProperty(value = "设备状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "device_status")
    private DeviceStatus deviceStatus;

    /**
     * 快速运动预警
     */
    @ApiModelProperty(value = "快速运动预警")
    @Column(name = "fast_run_warn")
    private Boolean fastRunWarn;

    /**
     * 人数超限
     */
    @ApiModelProperty(value = "人数超限")
    @Column(name = "people_count_limit_warn")
    private Boolean peopleCountLimitWarn;

    /**
     * 人群聚集
     */
    @ApiModelProperty(value = "人群聚集")
    @Column(name = "crowds_gather_warn")
    private Boolean crowdsGatherWarn;

    /**
     * 剧烈挥手预警
     */
    @ApiModelProperty(value = "剧烈挥手预警")
    @Column(name = "vigorously_waved_warn")
    private Boolean vigorouslyWavedWarn;

    /**
     * 打架预警
     */
    @ApiModelProperty(value = "打架预警")
    @Column(name = "fight_warn")
    private Boolean fightWarn;

    /**
     * 异常动作预警
     */
    @ApiModelProperty(value = "异常动作预警")
    @Column(name = "abnormal_action_warn")
    private Boolean abnormalActionWarn;

    /**
     * 快速运动预警预警值
     */
    @ApiModelProperty(value = "快速运动预警预警值")
    @Column(name = "fast_run_warn_limit")
    private Integer fastRunWarnLimit;

    /**
     * 人数超限预警值
     */
    @ApiModelProperty(value = "人数超限预警值")
    @Column(name = "people_count_warn_limit")
    private Integer peopleCountWarnLimit;

    /**
     * 人群聚集预警值
     */
    @ApiModelProperty(value = "人群聚集预警值")
    @Column(name = "crowds_gather_warn_limit")
    private Integer crowdsGatherWarnLimit;

    /**
     * 剧烈挥手预警预警值
     */
    @ApiModelProperty(value = "剧烈挥手预警预警值")
    @Column(name = "vigorously_waved_warn_limit")
    private Integer vigorouslyWavedWarnLimit;

    /**
     * 打架预警预警值
     */
    @ApiModelProperty(value = "打架预警预警值")
    @Column(name = "fight_warn_limit")
    private Integer fightWarnLimit;

    /**
     * 异常动作预警预警值
     */
    @ApiModelProperty(value = "异常动作预警预警值")
    @Column(name = "abnormal_action_warn_limit")
    private Integer abnormalActionWarnLimit;

    /**
     * 筛选
     */
    @ApiModelProperty(value = "筛选")
    @Enumerated(EnumType.STRING)
    @Column(name = "filter_type")
    private FilterType filterType;

    @OneToOne
    @JoinColumn(unique = true)
    private AlarmRegion alarmRegion;

    @OneToOne
    @JoinColumn(unique = true)
    private PerimeterProtectRegion perimeterProtectRegion;

    @OneToMany(mappedBy = "camera")
    @JsonIgnore
    private Set<AlarmHistory> alarmHistories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Camera name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCameraID() {
        return cameraID;
    }

    public Camera cameraID(String cameraID) {
        this.cameraID = cameraID;
        return this;
    }

    public void setCameraID(String cameraID) {
        this.cameraID = cameraID;
    }

    public String getCameraIP() {
        return cameraIP;
    }

    public Camera cameraIP(String cameraIP) {
        this.cameraIP = cameraIP;
        return this;
    }

    public void setCameraIP(String cameraIP) {
        this.cameraIP = cameraIP;
    }

    public String getBelongServer() {
        return belongServer;
    }

    public Camera belongServer(String belongServer) {
        this.belongServer = belongServer;
        return this;
    }

    public void setBelongServer(String belongServer) {
        this.belongServer = belongServer;
    }

    public String getBelongChannel() {
        return belongChannel;
    }

    public Camera belongChannel(String belongChannel) {
        this.belongChannel = belongChannel;
        return this;
    }

    public void setBelongChannel(String belongChannel) {
        this.belongChannel = belongChannel;
    }

    public String getCollectStandards() {
        return collectStandards;
    }

    public Camera collectStandards(String collectStandards) {
        this.collectStandards = collectStandards;
        return this;
    }

    public void setCollectStandards(String collectStandards) {
        this.collectStandards = collectStandards;
    }

    public DeviceStatus getDeviceStatus() {
        return deviceStatus;
    }

    public Camera deviceStatus(DeviceStatus deviceStatus) {
        this.deviceStatus = deviceStatus;
        return this;
    }

    public void setDeviceStatus(DeviceStatus deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Boolean isFastRunWarn() {
        return fastRunWarn;
    }

    public Camera fastRunWarn(Boolean fastRunWarn) {
        this.fastRunWarn = fastRunWarn;
        return this;
    }

    public void setFastRunWarn(Boolean fastRunWarn) {
        this.fastRunWarn = fastRunWarn;
    }

    public Boolean isPeopleCountLimitWarn() {
        return peopleCountLimitWarn;
    }

    public Camera peopleCountLimitWarn(Boolean peopleCountLimitWarn) {
        this.peopleCountLimitWarn = peopleCountLimitWarn;
        return this;
    }

    public void setPeopleCountLimitWarn(Boolean peopleCountLimitWarn) {
        this.peopleCountLimitWarn = peopleCountLimitWarn;
    }

    public Boolean isCrowdsGatherWarn() {
        return crowdsGatherWarn;
    }

    public Camera crowdsGatherWarn(Boolean crowdsGatherWarn) {
        this.crowdsGatherWarn = crowdsGatherWarn;
        return this;
    }

    public void setCrowdsGatherWarn(Boolean crowdsGatherWarn) {
        this.crowdsGatherWarn = crowdsGatherWarn;
    }

    public Boolean isVigorouslyWavedWarn() {
        return vigorouslyWavedWarn;
    }

    public Camera vigorouslyWavedWarn(Boolean vigorouslyWavedWarn) {
        this.vigorouslyWavedWarn = vigorouslyWavedWarn;
        return this;
    }

    public void setVigorouslyWavedWarn(Boolean vigorouslyWavedWarn) {
        this.vigorouslyWavedWarn = vigorouslyWavedWarn;
    }

    public Boolean isFightWarn() {
        return fightWarn;
    }

    public Camera fightWarn(Boolean fightWarn) {
        this.fightWarn = fightWarn;
        return this;
    }

    public void setFightWarn(Boolean fightWarn) {
        this.fightWarn = fightWarn;
    }

    public Boolean isAbnormalActionWarn() {
        return abnormalActionWarn;
    }

    public Camera abnormalActionWarn(Boolean abnormalActionWarn) {
        this.abnormalActionWarn = abnormalActionWarn;
        return this;
    }

    public void setAbnormalActionWarn(Boolean abnormalActionWarn) {
        this.abnormalActionWarn = abnormalActionWarn;
    }

    public Integer getFastRunWarnLimit() {
        return fastRunWarnLimit;
    }

    public Camera fastRunWarnLimit(Integer fastRunWarnLimit) {
        this.fastRunWarnLimit = fastRunWarnLimit;
        return this;
    }

    public void setFastRunWarnLimit(Integer fastRunWarnLimit) {
        this.fastRunWarnLimit = fastRunWarnLimit;
    }

    public Integer getPeopleCountWarnLimit() {
        return peopleCountWarnLimit;
    }

    public Camera peopleCountWarnLimit(Integer peopleCountWarnLimit) {
        this.peopleCountWarnLimit = peopleCountWarnLimit;
        return this;
    }

    public void setPeopleCountWarnLimit(Integer peopleCountWarnLimit) {
        this.peopleCountWarnLimit = peopleCountWarnLimit;
    }

    public Integer getCrowdsGatherWarnLimit() {
        return crowdsGatherWarnLimit;
    }

    public Camera crowdsGatherWarnLimit(Integer crowdsGatherWarnLimit) {
        this.crowdsGatherWarnLimit = crowdsGatherWarnLimit;
        return this;
    }

    public void setCrowdsGatherWarnLimit(Integer crowdsGatherWarnLimit) {
        this.crowdsGatherWarnLimit = crowdsGatherWarnLimit;
    }

    public Integer getVigorouslyWavedWarnLimit() {
        return vigorouslyWavedWarnLimit;
    }

    public Camera vigorouslyWavedWarnLimit(Integer vigorouslyWavedWarnLimit) {
        this.vigorouslyWavedWarnLimit = vigorouslyWavedWarnLimit;
        return this;
    }

    public void setVigorouslyWavedWarnLimit(Integer vigorouslyWavedWarnLimit) {
        this.vigorouslyWavedWarnLimit = vigorouslyWavedWarnLimit;
    }

    public Integer getFightWarnLimit() {
        return fightWarnLimit;
    }

    public Camera fightWarnLimit(Integer fightWarnLimit) {
        this.fightWarnLimit = fightWarnLimit;
        return this;
    }

    public void setFightWarnLimit(Integer fightWarnLimit) {
        this.fightWarnLimit = fightWarnLimit;
    }

    public Integer getAbnormalActionWarnLimit() {
        return abnormalActionWarnLimit;
    }

    public Camera abnormalActionWarnLimit(Integer abnormalActionWarnLimit) {
        this.abnormalActionWarnLimit = abnormalActionWarnLimit;
        return this;
    }

    public void setAbnormalActionWarnLimit(Integer abnormalActionWarnLimit) {
        this.abnormalActionWarnLimit = abnormalActionWarnLimit;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public Camera filterType(FilterType filterType) {
        this.filterType = filterType;
        return this;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    public AlarmRegion getAlarmRegion() {
        return alarmRegion;
    }

    public Camera alarmRegion(AlarmRegion alarmRegion) {
        this.alarmRegion = alarmRegion;
        return this;
    }

    public void setAlarmRegion(AlarmRegion alarmRegion) {
        this.alarmRegion = alarmRegion;
    }

    public PerimeterProtectRegion getPerimeterProtectRegion() {
        return perimeterProtectRegion;
    }

    public Camera perimeterProtectRegion(PerimeterProtectRegion perimeterProtectRegion) {
        this.perimeterProtectRegion = perimeterProtectRegion;
        return this;
    }

    public void setPerimeterProtectRegion(PerimeterProtectRegion perimeterProtectRegion) {
        this.perimeterProtectRegion = perimeterProtectRegion;
    }

    public Set<AlarmHistory> getAlarmHistories() {
        return alarmHistories;
    }

    public Camera alarmHistories(Set<AlarmHistory> alarmHistories) {
        this.alarmHistories = alarmHistories;
        return this;
    }

    public Camera addAlarmHistory(AlarmHistory alarmHistory) {
        this.alarmHistories.add(alarmHistory);
        alarmHistory.setCamera(this);
        return this;
    }

    public Camera removeAlarmHistory(AlarmHistory alarmHistory) {
        this.alarmHistories.remove(alarmHistory);
        alarmHistory.setCamera(null);
        return this;
    }

    public void setAlarmHistories(Set<AlarmHistory> alarmHistories) {
        this.alarmHistories = alarmHistories;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Camera camera = (Camera) o;
        if (camera.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), camera.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Camera{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", cameraID='" + getCameraID() + "'" +
            ", cameraIP='" + getCameraIP() + "'" +
            ", belongServer='" + getBelongServer() + "'" +
            ", belongChannel='" + getBelongChannel() + "'" +
            ", collectStandards='" + getCollectStandards() + "'" +
            ", deviceStatus='" + getDeviceStatus() + "'" +
            ", fastRunWarn='" + isFastRunWarn() + "'" +
            ", peopleCountLimitWarn='" + isPeopleCountLimitWarn() + "'" +
            ", crowdsGatherWarn='" + isCrowdsGatherWarn() + "'" +
            ", vigorouslyWavedWarn='" + isVigorouslyWavedWarn() + "'" +
            ", fightWarn='" + isFightWarn() + "'" +
            ", abnormalActionWarn='" + isAbnormalActionWarn() + "'" +
            ", fastRunWarnLimit=" + getFastRunWarnLimit() +
            ", peopleCountWarnLimit=" + getPeopleCountWarnLimit() +
            ", crowdsGatherWarnLimit=" + getCrowdsGatherWarnLimit() +
            ", vigorouslyWavedWarnLimit=" + getVigorouslyWavedWarnLimit() +
            ", fightWarnLimit=" + getFightWarnLimit() +
            ", abnormalActionWarnLimit=" + getAbnormalActionWarnLimit() +
            ", filterType='" + getFilterType() + "'" +
            "}";
    }
}
