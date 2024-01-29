package org.github.mbmll.example.csv;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.client.program.StreamContextEnvironment;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.csv.CsvReaderFormat;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.github.mbmll.example.jdbc.Device;

public class CsvDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamContextEnvironment.getExecutionEnvironment();
        FileSource<Device> source = FileSource.forRecordStreamFormat(CsvReaderFormat.forPojo(Device.class), new Path("D:\\repository\\something-starter\\starter-free-example\\flink-example\\data\\device.csv")).build();
        DataStreamSource<Device> device = environment.fromSource(source, WatermarkStrategy.forMonotonousTimestamps(), "device");

        System.out.println("Printing result to stdout. Use --output to specify output path.");
        device.print();
        environment.execute();
    }
}
