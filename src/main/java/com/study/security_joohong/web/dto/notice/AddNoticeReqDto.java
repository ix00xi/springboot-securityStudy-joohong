package com.study.security_joohong.web.dto.notice;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AddNoticeReqDto {
	private String noticeTitle;
	private String userCode;
	private String ir1;
	private List<MultipartFile> file;
}
