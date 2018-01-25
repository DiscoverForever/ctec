package com.cetc.cctv.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * 集群
 */
@ApiModel(description = "集群")
@Entity
@Table(name = "cluster")
@Document(indexName = "cluster")
public class Cluster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 视频服务器IP
     */
    @ApiModelProperty(value = "视频服务器IP")
    @Column(name = "video_server_ip")
    private String videoServerIp;

    /**
     * 视频通道数
     */
    @ApiModelProperty(value = "视频通道数")
    @Column(name = "video_channel_number")
    private Integer videoChannelNumber;

    /**
     * 视频服务器端口
     */
    @ApiModelProperty(value = "视频服务器端口")
    @Column(name = "video_server_port")
    private Integer videoServerPort;

    /**
     * 视频服务器用户名
     */
    @ApiModelProperty(value = "视频服务器用户名")
    @Column(name = "video_server_username")
    private String videoServerUsername;

    /**
     * 视频服务器密码
     */
    @ApiModelProperty(value = "视频服务器密码")
    @Column(name = "video_server_password")
    private String videoServerPassword;

    /**
     * 数据库IP
     */
    @ApiModelProperty(value = "数据库IP")
    @Column(name = "db_ip")
    private String dbIp;

    /**
     * 数据库名称
     */
    @ApiModelProperty(value = "数据库名称")
    @Column(name = "db_name")
    private String dbName;

    /**
     * 节点IP
     */
    @ApiModelProperty(value = "节点IP")
    @Column(name = "cluster_node_ip")
    private String clusterNodeIp;

    /**
     * 节点名称
     */
    @ApiModelProperty(value = "节点名称")
    @Column(name = "cluster_node_name")
    private String clusterNodeName;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoServerIp() {
        return videoServerIp;
    }

    public Cluster videoServerIp(String videoServerIp) {
        this.videoServerIp = videoServerIp;
        return this;
    }

    public void setVideoServerIp(String videoServerIp) {
        this.videoServerIp = videoServerIp;
    }

    public Integer getVideoChannelNumber() {
        return videoChannelNumber;
    }

    public Cluster videoChannelNumber(Integer videoChannelNumber) {
        this.videoChannelNumber = videoChannelNumber;
        return this;
    }

    public void setVideoChannelNumber(Integer videoChannelNumber) {
        this.videoChannelNumber = videoChannelNumber;
    }

    public Integer getVideoServerPort() {
        return videoServerPort;
    }

    public Cluster videoServerPort(Integer videoServerPort) {
        this.videoServerPort = videoServerPort;
        return this;
    }

    public void setVideoServerPort(Integer videoServerPort) {
        this.videoServerPort = videoServerPort;
    }

    public String getVideoServerUsername() {
        return videoServerUsername;
    }

    public Cluster videoServerUsername(String videoServerUsername) {
        this.videoServerUsername = videoServerUsername;
        return this;
    }

    public void setVideoServerUsername(String videoServerUsername) {
        this.videoServerUsername = videoServerUsername;
    }

    public String getVideoServerPassword() {
        return videoServerPassword;
    }

    public Cluster videoServerPassword(String videoServerPassword) {
        this.videoServerPassword = videoServerPassword;
        return this;
    }

    public void setVideoServerPassword(String videoServerPassword) {
        this.videoServerPassword = videoServerPassword;
    }

    public String getDbIp() {
        return dbIp;
    }

    public Cluster dbIp(String dbIp) {
        this.dbIp = dbIp;
        return this;
    }

    public void setDbIp(String dbIp) {
        this.dbIp = dbIp;
    }

    public String getDbName() {
        return dbName;
    }

    public Cluster dbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getClusterNodeIp() {
        return clusterNodeIp;
    }

    public Cluster clusterNodeIp(String clusterNodeIp) {
        this.clusterNodeIp = clusterNodeIp;
        return this;
    }

    public void setClusterNodeIp(String clusterNodeIp) {
        this.clusterNodeIp = clusterNodeIp;
    }

    public String getClusterNodeName() {
        return clusterNodeName;
    }

    public Cluster clusterNodeName(String clusterNodeName) {
        this.clusterNodeName = clusterNodeName;
        return this;
    }

    public void setClusterNodeName(String clusterNodeName) {
        this.clusterNodeName = clusterNodeName;
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
        Cluster cluster = (Cluster) o;
        if (cluster.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cluster.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cluster{" +
            "id=" + getId() +
            ", videoServerIp='" + getVideoServerIp() + "'" +
            ", videoChannelNumber=" + getVideoChannelNumber() +
            ", videoServerPort=" + getVideoServerPort() +
            ", videoServerUsername='" + getVideoServerUsername() + "'" +
            ", videoServerPassword='" + getVideoServerPassword() + "'" +
            ", dbIp='" + getDbIp() + "'" +
            ", dbName='" + getDbName() + "'" +
            ", clusterNodeIp='" + getClusterNodeIp() + "'" +
            ", clusterNodeName='" + getClusterNodeName() + "'" +
            "}";
    }
}
