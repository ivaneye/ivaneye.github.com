---
layout: post
date: 2017-01-08
title: 用户
categories: [private]
tags: [private,ppp]
avatarimg: "/img/head.jpg"
author: wangyifan
published: false

---

# 模块业务

- api：
- app：
- ba：
- common：
- dubbo-api：
- metadata：
- service-app：
- service-ba：

# 依赖模块与接口

- wechat-dubbo-api

```xml
<!-- 粉丝信息 -->
<dubbo:reference interface="com.qbao.scp.wechat.provider.FansInfoProvider" id="fansInfoProvider" version="1.0"/>
```

- finance-dubbo-api

```xml
<!-- 销售业绩 -->
<dubbo:reference interface="com.qbao.scp.finance.provider.PersonalTrackRecordProvider" id="personalTrackRecordProvider" version="1.0"/>
```

- cms-dubbo-api

```xml
<!-- 二维码 -->
<dubbo:reference interface="com.qbao.scp.cms.api.facade.IQRCodeService"  id="qRCodeService"/>
```

- cart-dubbo-api

```xml
<!-- 购物车历史记录 -->
<dubbo:reference interface="com.scp.csc.api.service.IHistoryRefereeProvider" id="historyRefereeProvider" version="1.0"/>
```

- order-dubbo-api

```xml
<!-- 采购查询 -->
<dubbo:reference interface="com.qbao.scp.service.PurchaseQueryService" id="purchaseQueryService" version="1.0"/>
```

- merchant-dubbo-api

```xml
<!-- 区域查询 -->
<dubbo:reference interface="com.qbao.scp.merchant.api.service.AreaProvider" id="areaProvider" version="1.0"/>
```

# 对外服务

```xml
<!-- SSO服务 -->
<dubbo:service interface="com.qbao.scp.user.api.facade.SSOService"  ref="ssoService" timeout="60000" version="1.0" />
<!-- 第三方账户绑定信息服务 -->
 <dubbo:service interface="com.qbao.scp.user.api.facade.UserBindInfoService"  ref="userBindInfoService" timeout="60000" version="1.0" />
 <!-- 用户关系及信息查询服务 -->
 <dubbo:service interface="com.qbao.scp.user.api.facade.IAgentUserService"  ref="agentUserService" timeout="60000" version="1.0" />
 <!-- 邮件服务 -->
 <dubbo:service interface="com.qbao.scp.user.api.facade.IAgentUserMailSenderService" ref="agentUserMailSenderService" timeout="6000" version="1.0" />
 <!-- 用户基本信息服务 -->
 <dubbo:service interface="com.qbao.scp.user.api.facade.UserBasicService" ref="userBasicService" timeout="6000" version="1.0" />
 <!-- 奖励规则服务 -->
 <dubbo:service interface="com.qbao.scp.user.api.facade.IAgentRewardRuleService" ref="agentRewardRuleService" timeout="6000" version="1.0"/>
```

# 登录流程
# 团队管理
