MultiTypeView
==================  

#1.简化RecyclerView的多种type的adapter
#2.ViewHolder的创建和绑定被提取出来变成ItemViewProvider，可以被多个adapter复用  
#3.支持一种数据对应多种ItemViewProvider  
#4.Fragment可以添加到RecyclerView上，实现复杂的界面  
   对于复杂的界面非常有利，一个复杂的界面可以分成多个Fragment，一个项目组分配给多个人开发.  
#5.RecyclerView上的Fragment是显示时候才加载
   具有懒加载的效果.假设RecyclerView上放了10个Fragment，你进去的时候只加载到开始的1,2个Fragment
#6.支持全局注册ItemViewProvider，和局部注册，和局部覆盖注册.
#7.列表的数据保存和恢复，以及fragment的保存和恢复  

Download sample [Apk](https://github.com/LuckyJayce/MultiTypeView/blob/master/raw/MutiTypeDemo.apk?raw=true)  

# 效果图 #
![image](https://github.com/LuckyJayce/MultiTypeView/blob/master/raw/1.png)  
# 关系图 #
![image](https://github.com/LuckyJayce/MultiTypeView/blob/master/raw/2.png)  
# 使用方法

实现ItemViewProvider

	public class MessageLeftProvider extends ItemViewProvider<Message> {
	
	    @Override
	    public RecyclerView.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int providerType) {
	        return new ItemViewHolder(inflater.inflate(R.layout.item_message_left, parent, false));
	    }
	
	    @Override
	    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Message message) {
	        ItemViewHolder vh = (ItemViewHolder) viewHolder;
	        vh.textView.setText(message.text);
	    }
	
	    private class ItemViewHolder extends RecyclerView.ViewHolder {
	
	        private final TextView textView;
	
	        public ItemViewHolder(View itemView) {
	            super(itemView);
	            textView = (TextView) itemView.findViewById(R.id.item_message_textView);
	        }
	    }
	}

Activity代码

	public class LongPageActivity extends AppCompatActivity {
	    private MultiTypeAdapter<Object> multiTypeAdapter;
	    private MultiTypeView multiTypeView;
	    private String myUserId = "1";
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_long_page);
	        multiTypeView = (MultiTypeView) findViewById(R.id.multiTypeView);
	        //带有FragmentManager的构造函数，默认帮您添加支持Fragment数据的FragmentHolderProvider
	        //也就是说你可以把Fragment放在adapter上使用，这里的Fragment只有第一次滑动到对应位置才会onCreateView的方法
	        ItemBinderFactory itemBinderFactory = new ItemBinderFactory(getSupportFragmentManager());
	        //有时候需要根据TextItem里面的某个字段，生成不同的布局.比如聊天界面的message是一样的，但是有区分左右布局
	        //ItemProviderSet可以通过数据类型区分无数种情况的Provider
	       itemBinderFactory.registerProvider(Message.class, new ItemViewProviderSet<Message>(new MessageProvider(MessageProvider.ALIGN_LEFT), new MessageProvider(MessageProvider.ALIGN_RIGHT)) {
                      @Override
                      protected int selectIndex(Message message) {
                          return myUserId.equals(message.userId) ? 1 : 0;
                      }
            });
	        multiTypeAdapter = new MultiTypeAdapter<>(loadData(0), itemBinderFactory);
	        multiTypeView.setAdapter(multiTypeAdapter);
	    }
	
	    private List<Object> loadData(int page) {
	        List<Object> data = new ArrayList<>();
	        TextView textView = new TextView(this);
	        textView.setText("第" + page + "页");
	        textView.setGravity(Gravity.CENTER);
	        textView.setBackgroundColor(Color.GRAY);
	        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtils.dipToPix(this, 100)));
	        data.add(textView);
	        data.add(new Message("1", "今天这个状态有所保留么？"));
            data.add(new Message("2", "没有保留！我已经，我已经用了洪荒之力啦！"));
	        data.add(new FragmentData(InfoLazyFragment.class, "InfoLazyFragment" + page));
	        data.add(new FragmentData(EditLazyFragment.class, "EditLazyFragment" + page));
	        data.add(new FragmentData(HotelLazyFragment.class, "HotelLazyFragment" + page));
	        data.add(new FragmentData(AirlineTicketLazyFragment.class, "AirlineTicketLazyFragment" + page));
	        data.add(new FragmentData(ShopLazyFragment.class, "ShopLazyFragment" + page));
	        data.add(new FragmentData(RecommendLazyFragment.class, "RecommendLazyFragment" + page));
	        data.add(new FragmentData(FoodLazyFragment.class, "FoodLazyFragment" + page));
	        data.add(new FragmentData(CultureLazyFragment.class, "CultureLazyFragment" + page));
	        this.page = page;
	        return data;
	    }
	}  
## 很简单吧，三个步骤  
**1.实现ItemViewProvider并注册到ItemBinderFactory上**  
  
**2.然后设置MultiTypeAdapter**  
   
**3.添加数据**   

# Gradle #

	compile 'com.shizhefei:MultiTypeView:1.0.0'
	由于用到了v4和recyclerview所以也要导入他们  
	compile 'com.android.support:support-v4:23.4.0'  
	compile 'com.android.support:recyclerview-v7:23.2.1'  
  
  
# 说明 #
    
**本项目是根据 https://github.com/drakeet/MultiType 类库的想法  
把ViewHolder的创建以ItemViewProvider的形式分离出来
，以及参考了部分代码实现.**  

**之后我添加了Fragment添加到RecyclerView上功能，以及view以数据的形式也可以添加上去.还有就是全局注册和局部注册的方案，ItemViewProviderSet实现了相同数据类型不同的ItemViewProvider.**  

**非常感谢** **@drakeet**

##主力类库##

**1.https://github.com/LuckyJayce/ViewPagerIndicator**  
Indicator 取代 tabhost，实现网易顶部tab，新浪微博主页底部tab，引导页，无限轮播banner等效果，高度自定义tab和特效

**2.https://github.com/LuckyJayce/MVCHelper**  
实现下拉刷新，滚动底部自动加载更多，分页加载，自动切换显示网络失败布局，暂无数据布局，支持任意view，支持切换主流下拉刷新框架。

**3.https://github.com/LuckyJayce/MultiTypeView**  
简化RecyclerView的多种type的adapter，Fragment可以动态添加到RecyclerView上，实现复杂的界面分多个模块开发

**4.https://github.com/LuckyJayce/EventBus**  
事件总线，通过动态代理接口的形式发布,接收事件。定义一个接口把事件发给注册并实现接口的类

**5.https://github.com/LuckyJayce/LargeImage**  
大图加载，可供学习

**6.https://github.com/LuckyJayce/GuideHelper**   
新手引导页，轻松的实现对应的view上面的显示提示信息和展示功能给用户  

有了这些类库，让你6的飞起

# 联系方式和问题建议

* 微博:http://weibo.com/u/3181073384
* QQ 群: 开源项目使用交流 :549284336

License
=======

    Copyright 2016 shizhefei（LuckyJayce）
    Copyright 2016 drakeet.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
