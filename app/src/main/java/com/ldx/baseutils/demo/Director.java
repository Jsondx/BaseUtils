package com.ldx.baseutils.demo;

/**
 * Created by babieta on 2018/11/30.
 */

public class Director {
    private Builder mBuild = null;

    public Director(Builder builder) {
        this.mBuild = builder;
    }

    //指挥装机人员组装电脑
    public void Construct(String cpu, String memory, String hd) {
        mBuild.buildCPU(cpu);
        mBuild.buildMemory(memory);
        mBuild.buildHD(hd);
    }

}
