package com.example.file;

import com.example.file.FileBean;
import org.hibernate.validator.constraints.EAN;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;


public interface FileRepository extends JpaRepository<FileBean,Integer>{

    @Query(value="select * from sqsx_file where sqsx_file.isdel = 0",nativeQuery = true)
    public List<FileBean> findAllF();

    @Query(value = "select * from sqsx_file where sqsx_file.isdel = 0 and sqsx_file.user_id =?1 ",nativeQuery = true)
    public List<FileBean> findByUNU(Integer userid);

//    @Query(value = "select * from sqsx_file join sqsx_download  on sqsx_file.id = sqsx_download.file_id join sqsx_user on sqsx_user.id = sqsx_download.user_id where sqsx_file.isdel =0  and sqsx_download.isdel = 0 and sqsx_user.isdel=0 and  sqsx_user.username =?1 ",nativeQuery = true)
//    public List<FileBean> findByUND(String user_name);

    @Query(value = "select * from sqsx_file where sqsx_file.isdel = 0 and sqsx_file.label_id =?1 ",nativeQuery = true)
    public List<FileBean> findByLU(String tag);

    @Query(value="select * from sqsx_file where sqsx_file.md5=?1 and sqsx_file.isdel = 0",nativeQuery = true)
    public FileBean findByMd5(String md5);

    @Query(value = "select *from sqsx_file where sqsx_file.filename = ?1",nativeQuery = true)
    public FileBean findByFileName(String filename);

    @Query(value = "select *from sqsx_file where sqsx_file.file_id = ?1",nativeQuery = true)
    public FileBean findByFileId(Integer fileid);

    //public List<FileBean> findByUsersName(String username);
}
