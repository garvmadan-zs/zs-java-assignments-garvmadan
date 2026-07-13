package assignment1;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SystemInfo {

    public static void main(String[] args) throws Exception {

        System.out.println("User Name      : " + System.getProperty("user.name"));
        System.out.println("Home Directory : " + System.getProperty("user.home"));

        Process cpu = Runtime.getRuntime()
                .exec("sysctl -n hw.ncpu");

        BufferedReader cpuReader =
                new BufferedReader(new InputStreamReader(cpu.getInputStream()));

        System.out.println("CPU Cores      : " + cpuReader.readLine());



        Process memory = Runtime.getRuntime()
                .exec("sysctl -n hw.memsize");

        BufferedReader memReader =
                new BufferedReader(new InputStreamReader(memory.getInputStream()));

        long memoryBytes = Long.parseLong(memReader.readLine());

        System.out.printf("Memory         : %.2f GB%n",
                memoryBytes / (1024.0 * 1024 * 1024));



        Process disk = Runtime.getRuntime()
                .exec("df -k /");

        BufferedReader diskReader =
                new BufferedReader(new InputStreamReader(disk.getInputStream()));

        diskReader.readLine(); // skip header

        String diskInfo = diskReader.readLine();
        String[] values = diskInfo.split("\\s+");

        long diskKB = Long.parseLong(values[1]);

        System.out.printf("Disk Size      : %.2f GB%n",
                diskKB / (1024.0 * 1024));



        Process os = Runtime.getRuntime()
                .exec("sw_vers");

        BufferedReader osReader =
                new BufferedReader(new InputStreamReader(os.getInputStream()));

        System.out.println("OS Information:");

        String line;
        while ((line = osReader.readLine()) != null) {
            System.out.println(line);
        }
    }
}

