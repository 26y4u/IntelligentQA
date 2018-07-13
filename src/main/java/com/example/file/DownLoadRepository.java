package com.example.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DownLoadRepository extends JpaRepository<DownloadBean,Integer> {
    @Query(value="select * from sqsx_download where sqsx_download.user_id = ?1 and isdel=0 ORDER BY time desc",nativeQuery = true)
    public  List<DownloadBean> findByUserId(Integer userid);
}
