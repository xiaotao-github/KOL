var layout = function(){
  var _menu_id = 100;   // 目录的id
  var _nav_id = 10000;  // 给nav添加的id
  var $contentNav = $('.layout-content-nav');
  var $contentBody = $('.layout-content-body');
  var menuIdObj = {}; // 用来保存该级目录是否已经打开了
  function initMenuRender(menu){
    var $menu = $('.menu');
    menu.forEach(function(item){
      var $item = createMenuItem(item);
      $menu.append($item);
    })

    function createMenuItem(item){
      _menu_id++;
      var $li = $('<li data-menu-id="'+ _menu_id +'"></li>');
      if(item.icon){
        $li.append('<i class="iconfont '+ item.icon +'"></i>')
      }
      $li.append('<div>' + (item.sub ? '<i class="iconfont icon-right"></i>': "") + item.name + '</div>');
      if(item.sub) {    // 如果有子集目录，则添加子集目录
        $li.addClass('has-sub');
        var $subMenu = $('<ul class="sub-menu"></ul>');
        item.sub.forEach(function(item){
          var $item = createMenuItem(item);
          $subMenu.append($item)
        })
        $li.append($subMenu);
      }else if(item.path){   // 否则如果有路径的
        $li.attr('data-path', item.path)
      }
      return $li;
    }
  }

  function initDefaultPage(defaultPage){
    // _nav_id++;

    $contentNav.append('<div class="nav-item active" data-id="'+ _nav_id +'"><span class="iconfont icon-shuaxin"></span><p>'+ defaultPage.name +'</p><b class="rt-side"><i class="iconfont icon-close"></i></b></div>')
    $contentBody.append('<div class="iframe-item active" data-id="'+ _nav_id +'"><iframe src="'+ defaultPage.path +'" frameborder="0" scrolling="0"></iframe></div>');   // 添加新的iframe

    // 设置高度的
    setIframeHeight();
  }

  function initSidebarEvents(){
    var $sider = $('.layout-sider');
    var $menuBox = $('.menu-box');
    var state = 'open'; // 当前的侧边栏状态, 侧边栏当前是展开，还是收缩状态
    if(state === 'open'){
      $sider.addClass('open');
      $menuBox.css('overflow', 'auto');
    }else{
      $sider.removeClass('open');
      $menuBox.css('overflow', 'initial');
    }
    

    // 底部展开伸缩按钮
    $('.menu-control').click(function(){
      if(state === 'open'){
        $sider.removeClass('open');
        $menuBox.css('overflow', 'initial');
        $('.menu .has-sub').removeClass('open');
        $('.sub-menu').slideUp(0);
        $('.menu .active').removeClass('active');
        state = 'close';
      }else{
        $sider.addClass('open');
        $menuBox.css('overflow', 'auto');
        $('.menu .has-sub').removeClass('open');
        $('.sub-menu').slideUp(0);
        $('.menu .active').removeClass('active');
        state = 'open';
      }
    })

    /*$('.menu>.has-sub').click(function(){
      if(state !== 'open') return;
      console.log(111)
      var $this = $(this);
      if($this.hasClass('open')){
        $this.removeClass('open');
        $this.find('>.sub-menu').slideUp();
      }else{
        $this.addClass('open');
        $this.find('>.sub-menu').slideDown();
      }
    })
    $('.menu .has-sub .has-sub').click(function(){
      var $this = $(this);
      if($this.hasClass('open')){
        $this.removeClass('open');
        $this.find('>.sub-menu').slideUp();
      }else{
        $this.addClass('open');
        $this.find('>.sub-menu').slideDown();
      }
    })*/

    $('.menu li').click(function(){
      var $this = $(this);
      if($this.parent().hasClass('menu') && state !== 'open'){
        return;
      }else if($this.hasClass('has-sub')){
        if($this.hasClass('open')){
          $this.removeClass('open');
          $this.find('>.sub-menu').slideUp();
        }else{
          $this.addClass('open');
          $this.find('>.sub-menu').slideDown();
        }
      }else{

        var path = $this.attr('data-path');
        var menuId = $this.attr('data-menu-id');  // 对应目录的id

        if(curConfig.mode === 'single'){  // 如果是在只能打开一次的模式下
          if(!menuIdObj[menuId]){  // 判断一下该节点是否曾经被打开过，若还没有打开，
            menuIdObj[menuId] = _nav_id;  // 用 _nav_id 保存到menuIdObj上，
          }else{ // 已经打开过，就切换到对应目录
            navToWithId(menuIdObj[menuId]);
            return;
          }
        }

        
        // 添加dom节点
        $contentNav.append('<div class="nav-item" data-menu-id="'+ menuId +'" data-id="'+ _nav_id +'"><span class="iconfont icon-shuaxin"></span><p>'+ $this.text() +'</p><b class="rt-side"><i class="iconfont icon-close"></i></b></div>')
        $contentBody.append('<div class="iframe-item" data-id="'+ _nav_id +'"><iframe src="'+ path +'" frameborder="0" scrolling="0"></iframe></div>');   // 添加新的iframe
        
        // 切换
        navToWithId(_nav_id);
        // 设置高度的
        setIframeHeight();
        
        _nav_id++;
      }
    })
    $('.sub-menu').click(function(e){
      e.stopPropagation();
    })

    // 收缩时候 hover 展开

    $('.menu>li').hover(function(){
      if(state === 'open') return;
      $(this).addClass('hover');
      $(this).find('>.sub-menu').show();
    },function(){
      if(state === 'open') return;
      $(this).removeClass('hover');
      $(this).find('>.sub-menu').hide();
    })
  }

  // 初始化导航的事件
  function initContentHandle(){
    $contentNav.click(function(e){
      e.stopPropagation();
      var tagName = e.target.tagName.toLowerCase();
      var length;
      var tabId;
      if(tagName === 'span'){  // 刷新按钮
        var $parent = $(e.target).parent();
        tabId = $parent.attr('data-id');
        navToWithId(tabId);
        var $contentBodyDiv = $contentBody.find('.iframe-item');
        length = $contentBodyDiv.length;
        for(var i=0;i<length;i++){  // 
          if($contentBodyDiv.eq(i).attr('data-id') === tabId ){
            var $iframe = $contentBodyDiv.eq(i).find('iframe');
            $iframe.attr('src', $iframe.attr('src'));
            break;
          }
        }
      }
      if(tagName === 'p' || tagName === 'b'){  // 选择切换标签
        var $parent = $(e.target).parent();
        tabId = $parent.attr('data-id');
        navToWithId(tabId);
      }
      if(tagName === 'i'){   // 关闭图标
        var $parent = $(e.target).parent().parent();
        console.log
        menuIdObj[$parent.attr('data-menu-id')] = null;
        console.log($parent)
        tabId = $parent.attr('data-id');
        var parentIndex = $parent.index();
        var isActive = $parent.hasClass('active');

        // 移除页面的显示
        $parent.remove();   // 移除该节点
        var $contentBodyDiv = $contentBody.find('.iframe-item');
        length = $contentBodyDiv.length;
        for(var i=0;i<length;i++){  // 移除对应的iframe盒子
          if($contentBodyDiv.eq(i).attr('data-id') === tabId ){
            $contentBodyDiv.eq(i).remove();
            break;
          }
        }

        if(isActive){
          console.log('is active')
          var $contentNavDiv = $contentNav.find('>div');
          // console.log($contentNav.find('>div').eq(index).length)
          if($contentNavDiv.eq(parentIndex).length === 0){
            parentIndex--;
          }
          var $curNavDiv = $contentNavDiv.eq(parentIndex);
          if($curNavDiv.length !== 0){  // 表示存在该节点的
            tabId = $curNavDiv.attr('data-id');
            navToWithId(tabId);
          }
          
          // console.log(index)
          // tabIndexTo(index);
        }
      }
    })
  }
  
  // 根据id来到达对应的标签
  function navToWithId(tabId){
    var $contentNavDiv = $contentNav.find('>div');
    var $contentBodyDiv = $contentBody.find('.iframe-item');
    var $menuLi = $('.menu li');
    var length = $contentNavDiv.length;
    var i;
    var menuId = 0;
    for(i=0;i<length;i++){
      if($contentNavDiv.eq(i).attr('data-id') == tabId ){
        menuId = $contentNavDiv.eq(i).attr('data-menu-id');
        $contentNavDiv.eq(i).addClass('active').siblings().removeClass('active');
        break;
      }
    }
    length = $contentBodyDiv.length;
    for(i=0;i<length;i++){
      if($contentBodyDiv.eq(i).attr('data-id') == tabId ){
        $contentBodyDiv.eq(i).addClass('active').siblings().removeClass('active');
        break;
      }
    }
    // 遍历目录
    $('.menu .active').removeClass('active');
    length = $menuLi.length;
    for(i=0;i<length;i++){
      if($menuLi.eq(i).attr('data-menu-id') == menuId ){
        var $curLi = $menuLi.eq(i);
        $curLi.closest('.menu >.has-sub').addClass('active'); // 父节点添加active
        $curLi.addClass('active');
        break;
      }
    }

  }
  
  // 用来设置iframe的高度的，为了兼容safari的
  function setIframeHeight(){
    $('iframe').each(function(index, item){
      var $item = $(item);
      $item.height($item.parent().parent().height()-23);
    })
  }
  
  $(window).resize(function(){
    setIframeHeight();
  })
  
  var defaultConfig = {
    menu: [],
    mode: 'multiple',   // 默认可以重复打开: multiple，否则只能single单个打开: single
    defaultPage: null, // 默认没有打开页面
  }

  var curConfig = {};

  return {
    init: function(config){

      curConfig.menu = config.menu || defaultConfig.menu;
      curConfig.mode = config.mode || defaultConfig.mode;
      curConfig.defaultPage = config.defaultPage || defaultConfig.defaultPage;

      // 初始化目录渲染
      initMenuRender(curConfig.menu);
      
      // 判断时候有默认页面
      if(curConfig.defaultPage){
        initDefaultPage(curConfig.defaultPage);
      }

      // 给侧边栏添加事件
      initSidebarEvents();
      
      // 初始化导航
      initContentHandle();
    }
  }

}()

