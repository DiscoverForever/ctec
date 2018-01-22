package com.cetc.cctv.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.cetc.cctv.domain.enumeration.AnalysisType;

import com.cetc.cctv.domain.enumeration.Priority;

import com.cetc.cctv.domain.enumeration.AnalysisStatus;

import com.cetc.cctv.domain.enumeration.VideoType;

/**
 * 分析任务
 */
@ApiModel(description = "分析任务")
@Entity
@Table(name = "analysis_task")
@Document(indexName = "analysistask")
public class AnalysisTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 分析类型
     */
    @ApiModelProperty(value = "分析类型")
    @Enumerated(EnumType.STRING)
    @Column(name = "analysis_type")
    private AnalysisType analysisType;

    /**
     * 优先级
     */
    @ApiModelProperty(value = "优先级")
    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    /**
     * 分析状态
     */
    @ApiModelProperty(value = "分析状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "analysis_status")
    private AnalysisStatus analysisStatus;

    /**
     * 视频类型
     */
    @ApiModelProperty(value = "视频类型")
    @Enumerated(EnumType.STRING)
    @Column(name = "video_type")
    private VideoType videoType;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    /**
     * 执行时间
     */
    @ApiModelProperty(value = "执行时间")
    @Column(name = "execution_at")
    private ZonedDateTime executionAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnalysisType getAnalysisType() {
        return analysisType;
    }

    public AnalysisTask analysisType(AnalysisType analysisType) {
        this.analysisType = analysisType;
        return this;
    }

    public void setAnalysisType(AnalysisType analysisType) {
        this.analysisType = analysisType;
    }

    public Priority getPriority() {
        return priority;
    }

    public AnalysisTask priority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public AnalysisStatus getAnalysisStatus() {
        return analysisStatus;
    }

    public AnalysisTask analysisStatus(AnalysisStatus analysisStatus) {
        this.analysisStatus = analysisStatus;
        return this;
    }

    public void setAnalysisStatus(AnalysisStatus analysisStatus) {
        this.analysisStatus = analysisStatus;
    }

    public VideoType getVideoType() {
        return videoType;
    }

    public AnalysisTask videoType(VideoType videoType) {
        this.videoType = videoType;
        return this;
    }

    public void setVideoType(VideoType videoType) {
        this.videoType = videoType;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public AnalysisTask createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getExecutionAt() {
        return executionAt;
    }

    public AnalysisTask executionAt(ZonedDateTime executionAt) {
        this.executionAt = executionAt;
        return this;
    }

    public void setExecutionAt(ZonedDateTime executionAt) {
        this.executionAt = executionAt;
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
        AnalysisTask analysisTask = (AnalysisTask) o;
        if (analysisTask.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), analysisTask.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnalysisTask{" +
            "id=" + getId() +
            ", analysisType='" + getAnalysisType() + "'" +
            ", priority='" + getPriority() + "'" +
            ", analysisStatus='" + getAnalysisStatus() + "'" +
            ", videoType='" + getVideoType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", executionAt='" + getExecutionAt() + "'" +
            "}";
    }
}
