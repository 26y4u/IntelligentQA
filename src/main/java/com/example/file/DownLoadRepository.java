package com.example.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DownLoadRepository extends JpaRepository<DownloadBean,Integer> {
}
