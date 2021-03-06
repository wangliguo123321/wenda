# wenda
暑假学习项目
项目思维导图（太大了显示不出来）
http://ww1.sinaimg.cn/large/b06adeeegy1g11exizt2gj233o2blx6p.jpg
![](http://ww1.sinaimg.cn/large/b06adeeegy1g11exizt2gj233o2blx6p.jpg)


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
    
    
    
### Redis的使用
#### Redis数据结构
     List：双向列表，适用于最新列表，关注列表
     lpush
     lpop
     blpop
     lindex
     lrange
     lrem
     linsert
     lset
     rpush
  Set：适用于无顺序的集合，点赞点踩，抽奖，已读，共同好友
    
     sdiff
     smembers
     sinter
     scard
   SortedSet：排行榜，优先队列
    
     zadd
     zscore
     zrange
     zcount
     zrank
     zrevrank
   Hash：对象属性，不定长属性数
    
     hset
     hget
     hgetAll
     hexists
     hkeys
     hvals
   KV：单一数值，验证码，PV，缓存
     set
     setex
     incr
     
  ### 异步队列：（观察者模式）
  1：事件先定义事件类型
  2：定义事件模型（EventType type;int actorId;int entityType;int entityId; int entityOwnerId）
  3：发生事件，序列化后保存到队列中
  4：对应事件的处理：
          
    1：通过继承（InitializingBean, ApplicationContextAware）
    取到所有的处理方法beans：（applicationContext.getBeansOfType( EventHandler.class )）
    2：定义Map <EventType, List <EventHandler>> config，
    往config里面加各种事件类型，Handler(利用beans取到事件类型entryTypes)
    3：事件反序列化（拿到队列最后已给元素储存为list，然后反序列化）
    4：通过遍历config
     for (EventHandler handler : config.get( eventModel.getType() )) 
                                handler.doHandle( eventModel );
                       
  ### 邮件的发送
    引入依赖
    实现service，util中进行修改；
  ### 关注的实现
      关于Redis的事务：利用reids的exec命令保证执行，不然就discard回滚
      followee/follower两个队列
    保证事务的两个函数
          public Transaction multi(Jedis jedis) {
                try {
                    return jedis.multi();
                } catch (Exception e) {
                    logger.error("发生异常" + e.getMessage());
                } finally {
                }
                return null;
            }
        
            public List<Object> exec(Transaction tx, Jedis jedis) {
                try {
                    return tx.exec(); //保证事务执行
                } catch (Exception e) {
                    logger.error("发生异常" + e.getMessage());
                    tx.discard();   //回滚
                } finally {
                    if (tx != null) {
                        try {
                            tx.close();
                        } catch (IOException ioe) {
                            // ..
                        }
                    }
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                return null;
            }
            //service中对于函数的运用
    public boolean follow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date date = new Date();
        // 实体的粉丝增加当前用户
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zadd(followerKey, date.getTime(), String.valueOf(userId));
        // 当前用户对这类实体关注+1
        tx.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx, jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }        
    
    
  timeline的实现
 
    private int id;
    private int type;
    private int userId;
    private Date createdDate;
    private String data;
    private JSONObject dataJSON = null;

    
