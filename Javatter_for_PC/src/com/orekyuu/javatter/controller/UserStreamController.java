package com.orekyuu.javatter.controller;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.UserMentionEntity;
import twitter4j.UserStreamAdapter;

import com.orekyuu.javatter.account.AccountManager;
import com.orekyuu.javatter.logic.UserStreamLogic;

/**
 * ユーザーストリームのController
 * 
 * @author orekyuu
 * 
 */
public class UserStreamController extends UserStreamAdapter {

	private UserStreamLogic model;

	/**
	 * モデルを設定
	 * 
	 * @param model
	 */
	public void setModel(UserStreamLogic model) {
		this.model = model;
	}

	@Override
	public void onStatus(Status status) {
		model.onStatus(status);
		try {
			if (isReply(status)) {
				model.onReplyTweet(status);
			}
			if (status.isRetweet()) {
				model.onRetweetTweet(status);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	private boolean isReply(Status status) throws IllegalStateException,
			TwitterException {
		if (status == null) {
			return false;
		}
		for (UserMentionEntity ume : status.getUserMentionEntities()) {
			if (ume.getScreenName().equals(
					AccountManager.getInstance().getAccessToken().getScreenName())) {
				return true;
			}
		}
		return false;
	}
}
