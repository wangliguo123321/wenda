# wenda
暑假学习项目
### 登陆注册的实现：三步骤：

#### 1: 使用拦截器：所有controller之前，留下插入点passportInterceptor；
通过继承HandlerIntercepter来实现三个方法：

  ![](http://ww1.sinaimg.cn/large/b06adeeegy1g0zogoyhh5j20vy06x74r.jpg)

1. preHandle：取到Cookies里面的token，并判断是否过期：
2. 新建hostHolder，储存登陆的用户；ThreadLocal<User> users = new ThreadLocal();
3. postHandle里面就可以拿到登陆的user；


#### 2. 将注册点注入到系统中；新建WendaWebConfiguration，
通过继承WebConfiguration，实现方法addInterceptors注册点；


#### 3. 重复步骤一，新建切点LoginRequiredInterceptor，继承HandlerIntercepter实现；
重复步骤二，在配置中注册，实现方法ddInterceptors注册点；

 1. preHandle中进行判断跳转：hostHolder里面没有用户，实现跳转到未登录界面：



    
    
### 字段树的过滤 四步骤：
1：读取关键词文本：
        
    public void afterPropertiesSet()  {
            rootNode = new TrieNode();

        try {
            InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                lineTxt = lineTxt.trim();
                addWord(lineTxt);
            }
            read.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }
 2： /**
     * 判断是否是一个符号，过滤掉颜表情等各种符号
     */
        
       private boolean isSymbol(char c) {
           int ic = (int) c;
           // 0x2E80-0x9FFF 东亚文字范围
           return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
       }
       
  3：  /**
          * 读取的文本生成前缀树
          * @param lineTxt
          */
          
         private void addWord(String lineTxt) {
             TrieNode tempNode = rootNode;
             // 循环每个字节
             for (int i = 0; i < lineTxt.length(); ++i) {
                 Character c = lineTxt.charAt(i);
                 // 过滤空格
                 if (isSymbol(c)) {
                     continue;
                 }
                 TrieNode node = tempNode.getSubNode(c);
     
                 if (node == null) { // 没初始化
                     node = new TrieNode();
                     tempNode.addSubNode(c, node);
                 }
     
                 tempNode = node;
     
                 if (i == lineTxt.length() - 1) {
                     // 关键词结束， 设置结束标志
                     tempNode.setKeywordEnd(true);
                 }
             }
         }   
  4：
      /**
      * 过滤敏感词
      */

     public String filter(String text) {
         if (StringUtils.isBlank(text)) {
             return text;
         }
                 String replacement = DEFAULT_REPLACEMENT;
                 StringBuilder result = new StringBuilder();
         
             TrieNode tempNode = rootNode;
             int begin = 0; // 回滚数
             int position = 0; // 当前比较的位置
     
             while (position < text.length()) {
                 char c = text.charAt(position);
                 // 空格直接跳过
                 if (isSymbol(c)) {
                     if (tempNode == rootNode) {
                         result.append(c);
                         ++begin;
                     }
                     ++position;
                     continue;
                 }
     
                 tempNode = tempNode.getSubNode(c);
     
                 // 当前位置的匹配结束
                 if (tempNode == null) {
                     // 以begin开始的字符串不存在敏感词
                     result.append(text.charAt(begin));
                     // 跳到下一个字符开始测试
                     position = begin + 1;
                     begin = position;
                     // 回到树初始节点
                     tempNode = rootNode;
                 } else if (tempNode.isKeywordEnd()) {
                     // 发现敏感词， 从begin到position的位置用replacement替换掉
                     result.append(replacement);
                     position = position + 1;
                     begin = position;
                     tempNode = rootNode;
                 } else {
                     ++position;
                 }
             }
     
             result.append(text.substring(begin));
     
             return result.toString();
         }
### 评论中心的实现：统一的评论服务，覆盖所有的实体评论
![](http://ww1.sinaimg.cn/large/b06adeeegy1g0zvicpd2mj20xe07q0t8.jpg)
  
    id
    content
    entity_id ->questionId/commentId
    entity_type ->question/comment
    created_date
    user_id
### 站内信的实现：
![](http://ww1.sinaimg.cn/large/b06adeeegy1g0zvgt6vjlj21030f1jsv.jpg)
    
    id
    from_id
    to_id
    content
    created_date
    has_read
    conversation_id
  
 负责的站内信列表的展示：


    select "INSERT_FIELDS, " , count(id) as id from 
    ( select * from ", TABLE_NAME," where from_id=#{userId} or to_id=#{userId} order by created_date desc) tt 
    group by conversation_id order by created_date desc limit #{offset}, #{limit}"}
    
    
    
