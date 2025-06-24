package jp.co.internous.team2504.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.team2504.model.domain.MstDestination;
import jp.co.internous.team2504.model.form.DestinationForm;
import jp.co.internous.team2504.model.mapper.MstDestinationMapper;
import jp.co.internous.team2504.model.mapper.TblCartMapper;
import jp.co.internous.team2504.model.mapper.TblPurchaseHistoryMapper;
import jp.co.internous.team2504.model.session.LoginSession;

/**
 * 決済に関する処理を行うコントローラー
 *
 * @author インターノウス
 *
 */
@Controller
@RequestMapping("/team2504/settlement")
public class SettlementController {

	/*
	 * フィールド定義
	 */
	private Gson gson = new Gson();

	@Autowired
	private MstDestinationMapper destinationMapper;

	@Autowired
	private TblCartMapper cartMapper;

	@Autowired
	private TblPurchaseHistoryMapper purchaseHistoryMapper;

	@Autowired
	private LoginSession session;

	/**
	 * 宛先選択・決済画面を初期表示する。
	 *
	 * @param m 画面表示用オブジェクト
	 * @return 宛先選択・決済画面
	 */
	@RequestMapping("/")
	public String index(Model m) {
		try {
			List<MstDestination> destinations = destinationMapper.findByUserId(session.getUserId());
			m.addAttribute("destinations", destinations);
		} catch (Exception e) {
			m.addAttribute("destinations", null);
		}
		m.addAttribute("loginSession", session);
		return "settlement";
	}

	/**
	 * 決済処理を行う
	 *
	 * @param destinationId 宛先情報id
	 * @return true:決済処理成功、false:決済処理失敗
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/complete")
	@ResponseBody
	public boolean complete(@RequestBody String destinationId) {
		DestinationForm form = gson.fromJson(destinationId, DestinationForm.class);
		int insertCount = purchaseHistoryMapper.insert(form.getDestinationId(), session.getUserId());
		if (insertCount > 0) {
			return cartMapper.deleteByUserId(session.getUserId()) > 0;
		}
		return false;
	}
}