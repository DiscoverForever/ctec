package com.cetc.cctv.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.cetc.cctv.domain.enumeration.AlarmType;

/**
 * 历史警报
 */
@ApiModel(description = "历史警报")
@Entity
@Table(name = "alarm_history")
@Document(indexName = "alarmhistory")
public class AlarmHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 报警截图
     */
    @ApiModelProperty(value = "报警截图")
    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    /**
     * 报警类型
     */
    @ApiModelProperty(value = "报警类型")
    @Enumerated(EnumType.STRING)
    @Column(name = "alarm_type")
    private AlarmType alarmType;

    /**
     * 报警时间
     */
    @ApiModelProperty(value = "报警时间")
    @Column(name = "jhi_time")
    private ZonedDateTime time;

    @ManyToOne
    private Camera camera;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public AlarmHistory image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public AlarmHistory imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public AlarmType getAlarmType() {
        return alarmType;
    }

    public AlarmHistory alarmType(AlarmType alarmType) {
        this.alarmType = alarmType;
        return this;
    }

    public void setAlarmType(AlarmType alarmType) {
        this.alarmType = alarmType;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public AlarmHistory time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public Camera getCamera() {
        return camera;
    }

    public AlarmHistory camera(Camera camera) {
        this.camera = camera;
        return this;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
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
        AlarmHistory alarmHistory = (AlarmHistory) o;
        if (alarmHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), alarmHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AlarmHistory{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", alarmType='" + getAlarmType() + "'" +
            ", time='" + getTime() + "'" +
            "}";
    }
}
