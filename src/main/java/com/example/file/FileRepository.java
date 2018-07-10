package com.example.file;

import com.example.file.FileBean;
import org.hibernate.validator.constraints.EAN;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


public interface FileRepository extends JpaRepository<FileBean,Integer>{

//    @Query(value = "select * from sqsx_file join sqsx_upload  on sqsx_file.id = sqsx_upload.file_id join sqsx_user on sqsx_user.id = sqsx_upload.user_id order by sqsx_upload.time where sqsx_file.isdel =0  and sqsx_upload.isdel = 0 and sqsx_user.isdel=0 and  sqsx_user.username =?1 ",nativeQuery = true)
//    public List<FileBean> findByUNU(String user_name);
    @Query(value = "select * from sqsx_file where sqsx_file.isdel = 0 and sqsx_file.user_id =?1 ",nativeQuery = true)
    public List<FileBean> findByUNU(Integer userid);

    @Query(value = "select * from sqsx_file join sqsx_download  on sqsx_file.id = sqsx_download.file_id join sqsx_user on sqsx_user.id = sqsx_download.user_id where sqsx_file.isdel =0  and sqsx_download.isdel = 0 and sqsx_user.isdel=0 and  sqsx_user.username =?1 ",nativeQuery = true)
    public List<FileBean> findByUND(String user_name);

    @Query(value = "select * from sqsx_file where sqsx_file.isdel = 0 and sqsx_file.label_id =?1 ",nativeQuery = true)
    public List<FileBean> findByLU(Integer label);

    //public List<FileBean> findByUsersName(String username);
}
