package com.home.service;


import com.home.dto.User;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyShiro extends AuthorizingRealm {
    @Autowired
    IUserService usi;

    //登录凭证
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken Token){
        //UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordToken token=(UsernamePasswordToken)Token;
        //获取身份信息
        String username=(String)Token.getPrincipal();
        //笔记根据用户名去数据库找密码
        //查出是否有此用户
        User user=usi.findByName(username);
        if (user!=null){
            //若存在，将此用户存放到登录认证info中,可以考虑写一个getName(),不写也行
            SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(),getName());
            //设置盐，用来核对密码
            info.setCredentialsSalt(ByteSource.Util.bytes("wenyuan"));
            return info;
        }
        return null;
    }
    //权限认证，principaCollection是身份信息
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection){
        //获取登录时输入的用户名
//        String loginName=(String)principalCollection.fromRealm(getName()).iterator().next();
        String username = (String)principalCollection.getPrimaryPrincipal();
        //到数据库中查看是否有此对象
        User user=usi.findUserAndRole(username);
        if (user!=null){
            //权限信息对象info，用来存放查出的用户所有的角色(role)和权限（permission）
            SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
            //用户的角色,这里用来拿到角色的名字
            String role=user.getUserRole().getRole_name();
            info.addRole(role);
            //用户的角色对应的所有权限，如果只是使用角色定义访问权限，下面的四行可以不要
//            List<Role> roleList=user.getRoleList();
//            for (Role role:roleList){
////        info.addStringPermission(role.getPermissionsName());这里需要名字
//            }
            return info;
        }
        return null;
    }
}
