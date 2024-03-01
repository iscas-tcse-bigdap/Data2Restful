package com.iscas.k8scli.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class K8sResource {
    private double totalCPU;
    private double usedCPU;
    private double cpuUTE; //利用率
    private double totalMemory;
    private double usedMemory;
    private double memUTE; //利用率

    public K8sResource() {
        this.totalCPU = 0.0;
        this.usedCPU = 0.0;
        this.cpuUTE = 0.0;
        this.totalMemory = 0.0;
        this.usedMemory = 0.0;
        this.memUTE = 0.0;

    }

    public K8sResource(double newTotalCPU, double newUsedCPU, double newCpuUTE, double newTotalMemory,
                       double newUsedMemory, double newMemUTE) {
        this.totalCPU = newTotalCPU;
        this.usedCPU = newUsedCPU;
        this.cpuUTE = newCpuUTE;
        this.totalMemory = newTotalMemory;
        this.usedMemory = newUsedMemory;
        this.memUTE = newMemUTE;
    }

    /**
     * 获取cpu利用率
     *
     * @return
     */
    public double getCpuUTE() {
        if (this.totalCPU == 0) {
            return 0.0;
        }
        return this.usedCPU / this.totalCPU;
    }

    /**
     * 获取内容利用率
     *
     * @return
     */
    public double getMemUTE() {
        if (this.totalMemory == 0) {
            return 0.0;
        }
        return this.usedMemory / this.totalMemory;
    }

    /**
     * 获取cpu空闲量
     *
     * @return cpu空闲量， 单位core
     */
    public double getFreeCpu() {
        return this.totalCPU - this.usedCPU;
    }

    /**
     * 获取cpu空闲比例
     *
     * @return
     */
    public double getFreeCpuRate() {
        if (this.totalCPU == 0) {
            return 0.0;
        }
        return 1.0 - this.getCpuUTE();
    }


    /**
     * 获取内存空闲量
     *
     * @return 内容空闲量， 单位Mi
     */
    public double getFreeMemory() {
        return this.totalMemory - this.usedMemory;
    }

    /**
     * 获取内存空闲比例
     *
     * @return
     */
    public double getFreeMemoryRate() {
        if (this.totalMemory == 0) {
            return 0.0;
        }
        return 1.0 - this.getMemUTE();
    }

    /**
     * @return tostring
     */
    @Override
    public String toString() {
        return "K8sResource{"
            + "totalCPU=" + totalCPU
            + ", usedCPU=" + usedCPU
            + ", cpu_UTE=" + cpuUTE
            + ", totalMemory=" + totalMemory
            + ", usedMemory=" + usedMemory
            + ", mem_UTE=" + memUTE
            + '}';
    }
}
