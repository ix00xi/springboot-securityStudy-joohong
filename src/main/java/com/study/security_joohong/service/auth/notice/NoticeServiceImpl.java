package com.study.security_joohong.service.auth.notice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.security_joohong.domain.notice.Notice;
import com.study.security_joohong.domain.notice.NoticeFile;
import com.study.security_joohong.domain.notice.NoticeRepository;
import com.study.security_joohong.web.dto.notice.AddNoticeReqDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
	
	@Value("${file.path}")
	private String filePath;
	
	private final NoticeRepository noticeRepository;
	
	@Override
	public int addNotice(AddNoticeReqDto addNoticeReqDto) throws Exception {
		Predicate<String> predicate = (filename) -> !filename.isBlank();
		
		Notice notice = null;
		
		
		notice = Notice.builder()
				.notice_title(addNoticeReqDto.getNoticeTitle())
				
				.notice_content(addNoticeReqDto.getIr1())
				.build();
		
		noticeRepository.saveNotice(notice);
		
		if(predicate.test(addNoticeReqDto.getFile().get(0).getOriginalFilename())) {
			List<NoticeFile> noticeFiles = new ArrayList<NoticeFile>();
			
			for(MultipartFile file : addNoticeReqDto.getFile()) {
				String originalFilename = file.getOriginalFilename();
				String tempFilename = UUID.randomUUID().toString().replaceAll("-", "") + "_" + originalFilename;
				log.info(tempFilename);
				
				Path uploadPath = Paths.get(filePath, "notice/" + tempFilename);
				
				File f = new File(filePath + "notice");
				if(!f.exists()) {
					f.mkdirs();
				}
				
				try {
					Files.write(uploadPath, file.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				noticeFiles.add(NoticeFile.builder().notice_code(notice.getNotice_code()).file_name(tempFilename).build());
			}
			
			noticeRepository.saveNoticeFiles(noticeFiles);
			
		}
		
		return notice.getNotice_code();
	}

	
}