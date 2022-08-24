package com.study.security_joohong.service.auth.notice;

import com.study.security_joohong.web.dto.notice.AddNoticeReqDto;

public interface NoticeService {
	public int addNotice(AddNoticeReqDto addNoticeReqDto) throws Exception;
}
