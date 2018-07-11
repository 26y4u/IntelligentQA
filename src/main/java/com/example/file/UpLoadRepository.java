package com.example.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UpLoadRepository extends JpaRepository<UploadBean,Integer> {
   // UploadBean findByUser_id(Integer userid);
   @Query(value = "select * from sqsx_upload where sqsx_upload.isdel = 0 and sqsx_upload.user_id=?1 ",nativeQuery = true)
   public List<UploadBean> findByUserId(Integer userid);

   @Query(value = "select * from sqsx_upload where sqsx_upload.isdel = 0 and sqsx_upload.user_id=?1 and sqsx_upload.file_id = ?2",nativeQuery = true)
   public UploadBean findByUserIdAndFileId(Integer userid, Integer fileid);
   //UploadBean findByUserId(Integer id);
}
