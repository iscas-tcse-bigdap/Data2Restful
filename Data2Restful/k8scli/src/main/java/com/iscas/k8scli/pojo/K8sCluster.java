package com.iscas.k8scli.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class K8sCluster {
    private String name;
    private String config;
    private String ipPort;
    private boolean enable;
    private K8sResource k8sResource;

    public K8sCluster() {
        this.k8sResource = new K8sResource();
    }

    public K8sCluster(String newName, String newConfig, String newIpPort, boolean newEnable) {
        this.name = newName;
        this.config = newConfig;
        this.ipPort = newIpPort;
        this.enable = newEnable;
    }

    @Override
    public String toString() {
        return "K8sCluster{"
                + "name='" + name + '\''
                + ", config='" + config + '\''
                + ", ipPort='" + ipPort + '\''
                + ", enable=" + enable
                + ", k8sResource=" + k8sResource
                + '}';
    }
}
