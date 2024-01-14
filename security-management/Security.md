##### org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter
根据请求封装获取WebAsyncManager,从WebAsyncManager获取/注册的安全上下文可调用处理拦截器

##### org.springframework.security.web.context.SecurityContextPersistenceFilter
SecurityContextPersistenceFilter主要是使用SecurityContextRepository在session中保存或更新一个SecurityContext,并将SecurityContext给以后的过滤器使用,来为后续filter建立所需的山下文.
SecurityContext中存储了当前用户的认证以及权限信息

##### org.springframework.security.web.header.HeaderWriterFilter
向请求的Header中添加相应的信息,可在http标签内部使用security:header来控制

##### org.springframework.web.filter.CorsFilter
csrf又称跨域请求伪造,SpringSecurity会对所有的post请求验证是否包含系统生成的csrf的token信息,如果不包括,则报错.起到防止csrf攻击的效果

##### org.springframework.security.web.authentication.logout.LogoutFilter
匹配URL为logout的请求, 实现用户退出清除认证信息

##### com.jiabb.filter.ValidateCodeFilter
自定义拦截器

##### org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter 
表单认证操作全靠这个过滤器, 默认匹配URL为login且必须为POST请求

##### org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter
如果没有在配置文件中指定认证页面,则有该过滤器生成一个默认认证的页面

##### org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter
如果没有在配置文件中指定认证页面,则有该过滤器生成一个默认退出页面的页面

##### org.springframework.security.web.session.ConcurrentSessionFilter


##### org.springframework.security.web.savedrequest.RequestCacheAwareFilter
通过HttpSessionRequestCache内部维护了一个RequestCache 用于缓存HttpServletRequest

##### org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter
针对ServletRequest进行一次包装, 使得request具有更加丰富的API

##### org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter


##### org.springframework.security.web.authentication.AnonymousAuthenticationFilter
当SecurityContextHolder中认证信息为空, 则会创建一个匿名用户存入到SecurityContextHolder中, spring security为了兼容未登录的访问,也走了一套认证流程,只不过是一个匿名的身份

##### org.springframework.security.web.session.SessionManagementFilter
SecurityContextRepository限制同一个用户开启多个会话的数量

##### org.springframework.security.web.access.ExceptionTranslationFilter
异常转换过滤器位于整个springSecurityFilterChain的后方, 用来转换整个链路中出现的异常

##### org.springframework.security.web.access.intercept.FilterSecurityInterceptor
获取所配置资源访问的授权信息, 根据SpringSecurityContextHolder中存储的用户信息来决定器是否有权限
