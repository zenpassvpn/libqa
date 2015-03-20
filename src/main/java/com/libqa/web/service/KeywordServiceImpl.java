package com.libqa.web.service;

import com.libqa.application.enums.KeywordTypeEnum;
import com.libqa.application.util.StringUtil;
import com.libqa.domain.Keyword;
import com.libqa.domain.KeywordList;
import com.libqa.repository.KeywordListRepository;
import com.libqa.repository.KeywordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * Created by yong on 2015-03-08.
 *
 * @author yong
 */
@Slf4j
@Service
public class KeywordServiceImpl implements KeywordService {

	@Autowired
	KeywordRepository keywordRepository;

	@Autowired
	KeywordListRepository keywordListRepository;

	/**
	 * 키워드를 저장한 후 통계 데이터를 생성한다.
	 * 키워드 리스트는 객체 (Space, Wiki, QaContent의 List 타입으로 넘어온다)
	 * @param keywordParams
	 * @param keywordType
	 * @return
	 */
	@Override
	@Transactional
	public boolean saveKeywordAndList(String[] keywordParams, KeywordTypeEnum keywordType, Integer entityId) {
		Assert.notNull(keywordParams, "키워드가 존재하지 않습니다.");
		Assert.notNull(keywordType, "키워드 타입이 존재하지 않습니다.");
		boolean result = false;

		try {
			for (int i = 0; i <= keywordParams.length; i++) {
				log.info("@ param : {}", keywordParams[i]);
				saveKeyword(keywordParams[i], keywordType, entityId);
				saveKeywordList(keywordParams[i], keywordType);
			}
			result = true;
		} catch (Exception e) {
			log.error("키워드 저장시 에러가 발생했습니다.", e.getMessage());
			result = false;
		}
		return result;
	}

	/**
	 * 키워드를 저장한다.
	 * @param param
	 * @param keywordType
	 * @param entityId
	 */
	@Transactional
	public void saveKeyword(String param, KeywordTypeEnum keywordType, Integer entityId) {
		Assert.notNull(StringUtil.defaultString(param, null), "키워드 값이 존재하지 않습니다.");
		log.info("# param : {}", param);
		log.info("# keywordType : {}", keywordType);
		log.info("# KeywordTypeEnum.SPACE : {}", KeywordTypeEnum.SPACE);

		try {
			Keyword keyword = new Keyword();

			if (keywordType.equals(KeywordTypeEnum.SPACE)) {
				keyword.setSpaceId(entityId);
			} else if (keywordType.equals(KeywordTypeEnum.WIKI)) {
				keyword.setWikiId(entityId);
			} else {
				keyword.setQaId(entityId);
			}

			keyword.setKeywordName(param);
			keyword.setKeywordType(keywordType);
			keyword.setInsertDate(new Date());
			keywordRepository.saveAndFlush(keyword);
		} catch (Exception e) {
			log.error("값 세팅 중 에러발생 ");
			e.printStackTrace();

		}

	}

	/**
	 * 키워드 리스트 통계 데이터를 생성한다. 없을 경우 insert, 존재할 경우 count + 1 update 를 수행한다.
	 * @param param
	 * @param keywordType
	 */

	@Transactional
	public void saveKeywordList(String param, KeywordTypeEnum keywordType) {
		Assert.notNull(StringUtil.defaultString(param, null), "키워드 값이 존재하지 않습니다.");

		log.info("@KeywordList keywordType : {}", keywordType);
		log.info("@KeywordList param : {}", param);
		try {
			KeywordList keywordList = keywordListRepository.findByKeywordNameAndKeywordType(param, keywordType);
			log.info("### keywordList : {}", keywordList);
			if (keywordList != null) {
				log.info("## 값이 존재");
				keywordList.setKeywordCount(keywordList.getKeywordCount() + 1);
			} else {
				log.info("## 값을 생성 ");
				keywordList = new KeywordList(param, keywordType, new Integer(1));
			}
			keywordListRepository.saveAndFlush(keywordList);
		} catch (Exception e) {
			log.error("## 키워드 통계 생성중 에러 발생 ", e.getMessage());
			e.printStackTrace();
		}

	}

}