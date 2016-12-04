<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h1>Youtube Viewer</h1>

<div id="ytplayer"></div>

<script>
  // Load the IFrame Player API code asynchronously.
  var tag = document.createElement('script');
  tag.src = "https://www.youtube.com/player_api";
  var firstScriptTag = document.getElementsByTagName('script')[0];
  firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

  // Replace the 'ytplayer' element with an <iframe> and
  // YouTube player after the API code downloads.
  var player;
  function onYouTubePlayerAPIReady() {
    player = new YT.Player('ytplayer', {
      height: '720',
      width: '1080',
      videoId: '',
	  playerVars: {
		autoplay: 1,
		playlist: '<c:forEach var="item" items="${pics}">${item.url},</c:forEach>',
		iv_load_policy: 3
	  }
    });
  }
</script>
<center>
    <a href="<c:url value="/picviewer/groups" />" style="font-size: 20px">Back</a>
</center>
