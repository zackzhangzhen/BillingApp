package weibo4j.examples.friendships;

import weibo4j.Friendships;
import weibo4j.Weibo;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

public class DestroyFriendshipsDestroyById {

	public static void main(String[] args) {
		String access_token = args[0];
		Weibo weibo = new Weibo();
		weibo.setToken(access_token);
		String uid = args[1];
		Friendships fm = new Friendships();
		try {
			User fv =fm.destroyFriendshipsDestroyById(uid);
			Log.logInfo(fv.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
