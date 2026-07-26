package com.zs.assignment7.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zs.assignment7.util.DataBaseCoonection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;

public class ExportDAO {
    private static final Logger logger=LoggerFactory.getLogger(ExportDAO.class);

    public void exportStudents(String filePath){
        String sql = """
                SELECT s.id, s.first_name, s.last_name, s.mobile, d.dept_name
                FROM students s
                JOIN student_department sd
                ON s.id=sd.student_id
                JOIN departments d
                ON sd.dept_id=d.dept_id""";
        try(Connection connection=DataBaseCoonection.getConnection();
            PreparedStatement ps =connection.prepareStatement(sql);
              BufferedWriter writer =
                    Files.newBufferedWriter(
                    Paths.get(filePath))){
            ps.setFetchSize(5000);
            ResultSet rs =
                    ps.executeQuery();

            writer.write(
                    "id,first_name,last_name,mobile,department"
            );

            writer.newLine();


            int count = 0;


            while(rs.next()){


                writer.write(
                        rs.getString("id") + "," +
                                rs.getString("first_name") + "," +
                                rs.getString("last_name") + "," +
                                rs.getString("mobile") + "," +
                                rs.getString("dept_name")
                );
                writer.newLine();


                count++;


                if(count % 50000 == 0){

                    logger.info(
                            "{} records exported",
                            count
                    );
                }

            }


            logger.info(
                    "Export completed. Total records: {}",
                    count
            );



        }
        catch (Exception e){
            logger.error("Export not possible ", e);
        }
    }
}
