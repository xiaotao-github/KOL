$(function(){
  var $userinfoPop = $('.userinfo-pop');  // 弹窗
  var $infoEditBox = $userinfoPop.find('#info-edit-box');  // 编辑信息的盒子
  var $controlBtn = $userinfoPop.find('.control-btn');  // 编辑/完成 按钮
  var $close = $userinfoPop.find('.icon-close');
  
  statusInit();
  bindEvent();
  

  function statusInit(){
    $controlBtn.removeClass('editing');
    // 一开始是只读状态
    $infoEditBox.find('input[type="text"]').each(function(index, item){
      var $item = $(item);
      $item.prop('readonly', true); 
      $item.addClass('readonly');
    })
  }
  function bindEvent(){
    

    // 打开用户弹窗 
    $('.btn-primary-jianta').click(function(){
      $userinfoPop.show();
    })

    // 关闭按钮事件
    $close.click(function(){
      $userinfoPop.hide();
      $controlBtn.removeClass('editing');
      $infoEditBox.find('input[type="text"]').each(function(index, item){
        var $item = $(item);
        $item.prop('readonly', true); 
        $item.addClass('readonly');
      })
    })
    
    // 控制按钮 编辑/完成
    $controlBtn.click(function(){
      if($controlBtn.hasClass('editing')){
        $controlBtn.removeClass('editing');
        $infoEditBox.find('input[type="text"]').each(function(index, item){
          var $item = $(item);
          $item.prop('readonly', true); 
          $item.addClass('readonly');
        })
        // 完成
        confirmEdit();
      }else{
        $controlBtn.addClass('editing');
        $infoEditBox.find('input[type="text"]').each(function(index, item){
          var $item = $(item);
          $item.prop('readonly', false); 
          $item.removeClass('readonly');
        })
      }
    })
  }
  
  // 确定修改完成
  function confirmEdit(){
    $infoEditBox.find('input[type="text"]').each(function(index, item){
      var $item = $(item);
      alert($item.prop('name') +' : '+ $item.val());
    })
  }

})