package com.example.Crowdsource.repo;

import com.example.Crowdsource.model.File;
import com.example.Crowdsource.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepo extends JpaRepository<File, Integer> {
    @Query("SELECT u FROM File u WHERE u.id = :id AND u.fileType = :fileType")
    File findSpecificFile(@Param("id") Integer id, @Param("fileType") String fileType);
}
