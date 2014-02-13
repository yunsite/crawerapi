xquery version "1.0";
declare variable $encoding := utf-8;
declare variable $rootNode := //BODY;
declare variable $topicRootNode := $rootNode//DIV[@action-type="feed_list_item"];

declare function local:getTopics($topicNodes as node()*) as node()*
{
	for $topicNode in $topicNodes
		let $status_id := string($topicNode/@mid)
		let $f_status_id := string($topicNode/@omid)
		let $content := $topicNode/DIV[@class="WB_feed_datail S_line2 clearfix"]/DIV[@class="WB_detail"]
		let $status := $content//DIV[@node-type="feed_list_content"]//text()
		let $time := $content/DIV[@class="WB_func clearfix"]//A[@class="S_link2 WB_time"]//text()
		let $original_content := $content/DIV[@class="WB_media_expand SW_fun2 S_line1 S_bg1"]
		let $original_person := $original_content//A[@class="WB_name S_func3"]//text()
		let $original_text := $original_content//DIV[@class="WB_text"]//text()
		let $original_time := $original_content//A[@node-type="feed_list_item_date"]//text()

		return 	
			(: 是否有转发  :)
				if(exists($original_content))
				then	
				(: 是否存在转发状态 ，可能有转发视频的情况 :)
					if(exists($original_text))
					then
						<topic>
							<id>{$status_id}</id>
					    	<content>{$status}</content>
						    <publishtime>{$time}</publishtime>
							<fid>{$f_status_id}</fid>
					    	<original_content>{$original_person}{":"}{$original_text}</original_content>
					    	<original_time>{$original_time}</original_time>
				    	</topic>
				    else
						<topic>
							<id>{$status_id}</id>
					    	<content>{$status}</content>
						    <publishtime>{$time}</publishtime>
				    	</topic>
				else	
						<topic>
							<id>{$status_id}</id>
					    	<content>{$status}</content>
						    <publishtime>{$time}</publishtime>
				    	</topic>
};

declare function local:getUser() as node()
{
	let $userNode := $rootNode//DIV[@class="detail"]
	let $userName := $userNode//DIV[@class="left"]/text()
	let $statusCount :=  $rootNode//DIV[@class="W_main_r"]//DIV[@class="W_rightModule"][1]//LI[@class="W_no_border"]//STRONGNODE-TYPE/text()
	return 
		<user nickname="{$userName}" statusCount="{$statusCount}"/> 	
			
};

declare function local:isend() as xs:string
{
	let $end := $rootNode//DIV[@class="W_loading"]/SPAN
	let $next := $rootNode//A[@action-type="feed_list_page_next"]/SPAN
	let $is_end := if(contains($end,"正在加载中，请稍候..."))
					then "status_loading"
					else if(contains($next,"下一页"))
						then "has_next_page"
						else "ended"
	return $is_end
};

(: CODE TO UPDATE - END :)
<weibo_topic>  
	<topics>
      {
      	let $topics := local:getTopics($topicRootNode)
      	return $topics
      }
      
    </topics>
    <end>
	    {
	    	let $end := local:isend()
	    	return $end
	    }
    </end>
</weibo_topic>