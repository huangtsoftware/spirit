package com.kk.spirit.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

import com.kk.spirit.entity.Memory;
import com.kk.spirit.entity.SystemInfo;

public class SystemInfoUtil {

	
	public static SystemInfo property() {
		SystemInfo sys = new SystemInfo();
		
        Runtime r = Runtime.getRuntime();
        Properties props = System.getProperties();
        InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String ip = addr.getHostAddress();
        Map<String, String> map = System.getenv();
        String userName = map.get("USERNAME");// 获取用户名
        String computerName = map.get("COMPUTERNAME");// 获取计算机名
        System.out.println("用户名:    " + userName);
        sys.setSysname(userName);
        System.out.println("计算机名:    " + computerName);
        sys.setComname(computerName);
        System.out.println("本地ip地址:    " + ip);
        sys.setSysip(ip);
        System.out.println("JVM可以使用的总内存:    " + r.totalMemory() / 1024L);
        sys.setTotalMemory(r.totalMemory() / 1024L);
        System.out.println("JVM可以使用的剩余内存:    " + r.freeMemory() / 1024L);
        sys.setFreeMemory(r.freeMemory() / 1024L);
        System.out.println("JVM可以使用的处理器个数:    " + r.availableProcessors());
        sys.setAvailableProcessors(r.availableProcessors());
        System.out.println("Java的运行环境版本：    " + props.getProperty("java.version"));
        sys.setVersion(props.getProperty("java.version"));
        System.out.println("Java的安装路径：    " + props.getProperty("java.home"));
        sys.setHome(props.getProperty("java.home"));
        System.out.println("操作系统的名称：    " + props.getProperty("os.name"));
        sys.setOsname(props.getProperty("os.name"));
        System.out.println("操作系统的构架：    " + props.getProperty("os.arch"));
        sys.setOsarch(props.getProperty("os.arch"));
        System.out.println("操作系统的版本：    " + props.getProperty("os.version"));
        sys.setOsversion(props.getProperty("os.version"));
        return sys;
    }

	public static List<com.kk.spirit.entity.CpuInfo> cpu() {
		List<com.kk.spirit.entity.CpuInfo> cpus = new ArrayList<>();
		try {
			Sigar sigar = new Sigar();
			CpuInfo infos[];
			infos = sigar.getCpuInfoList();
			CpuPerc cpuList[] = null;
			cpuList = sigar.getCpuPercList();
			for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
				com.kk.spirit.entity.CpuInfo cpu = new com.kk.spirit.entity.CpuInfo();
				CpuInfo info = infos[i];
				System.out.println("第" + (i + 1) + "块CPU信息");
            	System.out.println("CPU的总量MHz:    " + info.getMhz());// CPU的总量MHz
            	cpu.setMhz(info.getMhz());
            	System.out.println("CPU生产商:    " + info.getVendor());// 获得CPU的卖主，如：Intel
            	cpu.setVendor(info.getVendor());
            	System.out.println("CPU类别:    " + info.getModel());// 获得CPU的类别，如：Celeron
            	cpu.setModel(info.getModel());
            	System.out.println("CPU缓存数量:    " + info.getCacheSize());// 缓冲存储器数量
            	cpu.setSize(info.getCacheSize());
            	System.out.println("CPU用户使用率:    " + CpuPerc.format(cpuList[i].getUser()));// 用户使用率
            	cpu.setUser(CpuPerc.format(cpuList[i].getUser()));
                System.out.println("CPU系统使用率:    " + CpuPerc.format(cpuList[i].getSys()));// 系统使用率
                cpu.setSys(CpuPerc.format(cpuList[i].getSys()));
                System.out.println("CPU当前等待率:    " + CpuPerc.format(cpuList[i].getWait()));// 当前等待率
                cpu.setWait(CpuPerc.format(cpuList[i].getWait()));
                System.out.println("CPU当前错误率:    " + CpuPerc.format(cpuList[i].getNice()));// 当前错误率
                cpu.setNice(CpuPerc.format(cpuList[i].getNice()));
                System.out.println("CPU当前空闲率:    " + CpuPerc.format(cpuList[i].getIdle()));// 当前空闲率
                cpu.setIdle(CpuPerc.format(cpuList[i].getIdle()));
                System.out.println("CPU总的使用率:    " + CpuPerc.format(cpuList[i].getCombined()));// 总的使用率
                cpu.setCombined(CpuPerc.format(cpuList[i].getCombined()));
                cpus.add(cpu);
			}
		} catch (SigarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cpus;
    }
	
	public static Memory memory()  {
		Memory memory = new Memory();
		try {
			Sigar sigar = new Sigar();
	        Mem mem = sigar.getMem();
	        // 内存总量
	        System.out.println("内存总量:    " + mem.getTotal() / 1024L + "K av");
	        memory.setMtotal(mem.getTotal() / 1024L);
	        // 当前内存使用量
	        System.out.println("当前内存使用量:    " + mem.getUsed() / 1024L + "K used");
	        memory.setMused(mem.getUsed() / 1024L);
	        // 当前内存剩余量
	        System.out.println("当前内存剩余量:    " + mem.getFree() / 1024L + "K free");
	        memory.setMfree(mem.getFree() / 1024L);
	        Swap swap = sigar.getSwap();
	        // 交换区总量
	        System.out.println("交换区总量:    " + swap.getTotal() / 1024L + "K av");
	        memory.setStotal(swap.getTotal() / 1024L);
	        // 当前交换区使用量
	        System.out.println("当前交换区使用量:    " + swap.getUsed() / 1024L + "K used");
	        memory.setSused(swap.getUsed() / 1024L);
	        // 当前交换区剩余量
	        System.out.println("当前交换区剩余量:    " + swap.getFree() / 1024L + "K free");
	        memory.setSfree(swap.getFree() / 1024L);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        
		return memory;
    }
	
	
	public static void main(String[] args) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				cpu();
				memory();
				property();
			}
		}, 1000, 2000);
		
	}
}
