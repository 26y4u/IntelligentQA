package com.example.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UpLoadRepository extends JpaRepository<UploadBean,Integer> {
   // UploadBean findByUser_id(Integer userid);
   @Query(value = "select * from sqsx_upload  where isdel = 0 and user_id = ?1 ",nativeQuery = true)
   List<UploadBean> selectByUserId(Integer userid);

   //List<UploadBean> findByUser_id(Integer userid);

   @Query(value = "select * from sqsx_upload where sqsx_upload.isdel = 0 and sqsx_upload.file_id = ?1",nativeQuery = true)
   public UploadBean selectByFileId(Integer fileid);
   //UploadBean findByUserId(Integer id);
}
