package com.pxy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pxy.reggie.common.R;
import com.pxy.reggie.entity.User;
import com.pxy.reggie.service.UserService;

import com.pxy.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-28 15:07
 **/

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;
    //请求手机验证码
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //生成随机的4为验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);
            //调用阿里云提供的短信服务api完成发送短信
            // SMSUtils.sendMessage("餐厅登录","SMS_275045519",phone,code);
            //存入code以进行比较
            session.setAttribute(phone, code);
            //设置验证码时间，并将验证码缓存到Redis中
            redisTemplate.opsForValue().set(phone,code,5,TimeUnit.MINUTES);

            return R.success("验证码发送成功！");
        }
        return R.error("短信发送失败");
    }

    //客户端登录
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
//        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从Session中获取保存的验证码
//        Object codeInSession = session.getAttribute(phone);

        //从redis中获取验证码
        Object codeInSession = redisTemplate.opsForValue().get(phone);
        if (codeInSession != null && code.equals(codeInSession)) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
            wrapper.eq(User::getPhone, phone);
            User user = userService.getOne(wrapper);
            if (user == null) {
                //判断是否是新用户
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            //如果用户登录成功，即可删除Redis中验证码
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登录失败");
    }

    /**
     * 用户退出
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
}