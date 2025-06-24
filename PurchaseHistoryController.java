package jp.co.internous.team2504.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.internous.team2504.model.domain.dto.PurchaseHistoryDto;
import jp.co.internous.team2504.model.mapper.TblPurchaseHistoryMapper;
import jp.co.internous.team2504.model.session.LoginSession;

/**
 * 購入履歴情報に関する処理を行うコントローラー
 *
 * @author インターノウス
 *
 */
@Controller
@RequestMapping("/team2504/history")
public class PurchaseHistoryController {

	/*
	 * フィールド定義
	 */
	@Autowired
	private TblPurchaseHistoryMapper purchaseHistoryMapper;

	@Autowired
	private LoginSession session;

	/**
	 * 購入履歴画面を初期表示する。
	 *
	 * @param m 画面表示用オブジェクト
	 * @return 購入履歴画面
	 */
	@RequestMapping("/")
	public String index(Model m) {
		List<PurchaseHistoryDto> historyList = purchaseHistoryMapper.findByUserId(session.getUserId());

		m.addAttribute("historyList", historyList);
		m.addAttribute("loginSession", session);

		return "purchase_history";
	}

	/**
	 * 購入履歴情報を論理削除する
	 *
	 * @return true:削除成功、false:削除失敗
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public boolean delete() {
		try {
			return purchaseHistoryMapper.logicalDeleteByUserId(session.getUserId()) > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}