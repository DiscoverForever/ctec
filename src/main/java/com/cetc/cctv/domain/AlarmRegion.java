package com.cetc.cctv.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * 报警区域
 */
@ApiModel(description = "报警区域")
@Entity
@Table(name = "alarm_region")
@Document(indexName = "alarmregion")
public class AlarmRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 左上X
     */
    @ApiModelProperty(value = "左上X")
    @Column(name = "left_up_x")
    private Float leftUpX;

    /**
     * 左上Y
     */
    @ApiModelProperty(value = "左上Y")
    @Column(name = "left_up_y")
    private Float leftUpY;

    /**
     * 右上X
     */
    @ApiModelProperty(value = "右上X")
    @Column(name = "right_up_x")
    private Float rightUpX;

    /**
     * 右上Y
     */
    @ApiModelProperty(value = "右上Y")
    @Column(name = "right_up_y")
    private Float rightUpY;

    /**
     * 左下X
     */
    @ApiModelProperty(value = "左下X")
    @Column(name = "left_down_x")
    private Float leftDownX;

    /**
     * 左下Y
     */
    @ApiModelProperty(value = "左下Y")
    @Column(name = "left_down_y")
    private Float leftDownY;

    /**
     * 右下X
     */
    @ApiModelProperty(value = "右下X")
    @Column(name = "right_down_x")
    private Float rightDownX;

    /**
     * 右下Y
     */
    @ApiModelProperty(value = "右下Y")
    @Column(name = "right_down_y")
    private Float rightDownY;

    @ManyToOne
    private Camera camera;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getLeftUpX() {
        return leftUpX;
    }

    public AlarmRegion leftUpX(Float leftUpX) {
        this.leftUpX = leftUpX;
        return this;
    }

    public void setLeftUpX(Float leftUpX) {
        this.leftUpX = leftUpX;
    }

    public Float getLeftUpY() {
        return leftUpY;
    }

    public AlarmRegion leftUpY(Float leftUpY) {
        this.leftUpY = leftUpY;
        return this;
    }

    public void setLeftUpY(Float leftUpY) {
        this.leftUpY = leftUpY;
    }

    public Float getRightUpX() {
        return rightUpX;
    }

    public AlarmRegion rightUpX(Float rightUpX) {
        this.rightUpX = rightUpX;
        return this;
    }

    public void setRightUpX(Float rightUpX) {
        this.rightUpX = rightUpX;
    }

    public Float getRightUpY() {
        return rightUpY;
    }

    public AlarmRegion rightUpY(Float rightUpY) {
        this.rightUpY = rightUpY;
        return this;
    }

    public void setRightUpY(Float rightUpY) {
        this.rightUpY = rightUpY;
    }

    public Float getLeftDownX() {
        return leftDownX;
    }

    public AlarmRegion leftDownX(Float leftDownX) {
        this.leftDownX = leftDownX;
        return this;
    }

    public void setLeftDownX(Float leftDownX) {
        this.leftDownX = leftDownX;
    }

    public Float getLeftDownY() {
        return leftDownY;
    }

    public AlarmRegion leftDownY(Float leftDownY) {
        this.leftDownY = leftDownY;
        return this;
    }

    public void setLeftDownY(Float leftDownY) {
        this.leftDownY = leftDownY;
    }

    public Float getRightDownX() {
        return rightDownX;
    }

    public AlarmRegion rightDownX(Float rightDownX) {
        this.rightDownX = rightDownX;
        return this;
    }

    public void setRightDownX(Float rightDownX) {
        this.rightDownX = rightDownX;
    }

    public Float getRightDownY() {
        return rightDownY;
    }

    public AlarmRegion rightDownY(Float rightDownY) {
        this.rightDownY = rightDownY;
        return this;
    }

    public void setRightDownY(Float rightDownY) {
        this.rightDownY = rightDownY;
    }

    public Camera getCamera() {
        return camera;
    }

    public AlarmRegion camera(Camera camera) {
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
        AlarmRegion alarmRegion = (AlarmRegion) o;
        if (alarmRegion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), alarmRegion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AlarmRegion{" +
            "id=" + getId() +
            ", leftUpX=" + getLeftUpX() +
            ", leftUpY=" + getLeftUpY() +
            ", rightUpX=" + getRightUpX() +
            ", rightUpY=" + getRightUpY() +
            ", leftDownX=" + getLeftDownX() +
            ", leftDownY=" + getLeftDownY() +
            ", rightDownX=" + getRightDownX() +
            ", rightDownY=" + getRightDownY() +
            "}";
    }
}
