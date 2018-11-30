package com.ldx.baseutils.demo;

/**
 *
 * @author babieta
 * @date 2018/11/30
 */

public class ConcreteBuilder extends Builder {

    private Computer computer = new Computer();

    @Override
    public void buildCPU(String cpu) {

    }

    @Override
    public void buildMemory(String memory) {

    }

    @Override
    public void buildHD(String hd) {

    }

    @Override
    public Computer create() {
        return computer;
    }
}
