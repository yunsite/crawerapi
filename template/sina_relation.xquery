xquery version "1.0";
declare variable $encoding := utf-8;
declare variable $rootNode := //BODY;
declare variable $followerRootNodes := $rootNode//LI[@class="clearfix S_line1"];

declare function local:getFollower($followerNodes as node()*) as node()*
{
	for $followerNode in $followerNodes
		let $follower := string($followerNode/@action-data)
		let $uid := substring-before(substring-after($follower,"uid="),"&amp;")
		let $fnick := substring-before(substring-after($follower,"fnick="),"&amp;")
		let $sex := substring-after($follower,"sex=")
		let $info := string($followerNode//DIV[@class="info"])
	return 
		<follower>
			<id>{$uid}</id>
			<fnick>{$fnick}</fnick>
			<sex>{$sex}</sex>
			<info>{$info}</info> 
		</follower>
};

declare function local:getTotalPage() as xs:string
{
	let $pages := $rootNode/DIV[@class="patch_title"]/text()
	let $count := substring-before(substring-after($pages,"关注了"),"人")
	return 
		$count
};

declare function local:isend() as xs:string
{
	let $pages := $rootNode//DIV[@class="W_pages W_pages_comment"]
	let $count := count($pages/A[@class="W_btn_c"])
	let $text := $pages/A[@class="W_btn_c"]/SPAN/text()

	return if($count = 1 and contains($text,"上一页"))
			then "ended"
			else "next"
};


(: CODE TO UPDATE - END :)
<weibo_focus>  
	<count>
	    {
		    let $count := local:getTotalPage()
		   	return $count
	    }
    </count>
	<focus>
      {
      	let $follower := local:getFollower($followerRootNodes)
      	return $follower
      }
      
    </focus>
    <end>
    	{
    		let $end := local:isend()
    		return $end	
    	}
    </end>
</weibo_focus>