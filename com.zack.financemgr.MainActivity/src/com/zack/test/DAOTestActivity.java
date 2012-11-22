package com.zack.test;

import android.os.Bundle;

import com.zack.financemgr.activity.BaseActivity;
import com.zack.financemgr.activity.R;
import com.zack.persistent.dao.MemberDAO;

public class DAOTestActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dao);
        
        MemberDAO memberDAO = new MemberDAO(this);
        
        memberDAO.dropTable();
        memberDAO.createTable();
        /*Member member = new Member();
        member.setEmail("zackzhangzhen@gmail.com");
        member.setName("Zachary");
        memberDAO.createEntity(member);
        
        List<Member> list = memberDAO.queryAllEntities();
        
        Member m = list.get(0);
        m.setWeibo("ZhangZhenZacFul");
        
        memberDAO.updateEntity(m, m.getId());
        
        m = list.get(1);
        memberDAO.deleteEntity(m.getId());*/
    }
}
