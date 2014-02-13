xquery version "1.0";
declare variable $encoding := utf-8;
declare variable $rootNode := //BODY;

declare function local:getInfo($root as node()*) as node()*
{
	let $infos := $root/DIV[@class="infoblock"]//DIV[@class="pf_item clearfix"]
	
		for $info in $infos
			let $con_ps := $info//DIV[@class="con"]/P
			let $con_as := $info//DIV[@class="con"]/A
			return
				if(count($con_ps)=0) 
				then 
					if(count($con_as)=0)
					then
						local:getCons($info)
					else
						local:getConas($info)	
				else
					local:getConps_type($info)
};

declare function local:getCons($root as node()*) as node()*
{
	let $type := $root/DIV[@class="label S_txt2"]/text()
	let $cons := $root//DIV[@class="con"]//text()
	return
		<item>
			<type>{$type}</type>
			<con>{$cons}</con>
		</item>
};

declare function local:getConps_type($root as node()*) as node()*
{
	let $types := $root/DIV[@class="label S_txt2"]
	let $cons := $root/DIV[@class="con"]
	let $count := count($types)
	for $x in (1 to $count)
		return
			<item>
				<type>{$types[$x]/text()}</type>
				<con>{$cons[$x]/P//text()}</con>
			</item>
};

declare function local:getConps_con($root as node()*) as node()*
{
	let $type := $root/DIV[@class="label S_txt2"]/text()
	let $cons := $root//DIV[@class="con"]/P
	
	for $con in $cons
	return
		<con>{$con//text()}</con>
};

declare function local:getConas($root as node()*) as node()*
{
	let $type := $root/DIV[@class="label S_txt2"]/text()
	let $cons := $root//DIV[@class="con"]/A
	
	for $con in $cons
	return
		<con>{$con//text()}</con>
};

declare function local:getMedalInfo() as node()*
{
	let $medals := $rootNode/DIV[@id="Grow"]//DIV[@node-type="medal"]//LI
	for $medal in $medals
	return
		<con>{string($medal/A/@title)}</con>
};

declare function local:getGrowInfo($root as node()*) as node()*
{
	let $info := $root//text()
	return
		<info>{$info}</info>
};

declare function local:getMain() as node()*
{
	let $link := string($rootNode/DIV[@class="pf_head_pic"]/IMG/@src)
	let $main := $rootNode/UL[@class="user_atten clearfix user_atten_l"]
	let $focus := $main/LI[1]/A/STRONG/text()
	let $fans := $main/LI[2]/A/STRONG/text()
	let $weibo := $main/LI[3]/A/STRONG/text()
	return 
		<Photo>
			<img>{$link}</img>
			<focus>{$focus}</focus>
			<fans>{$fans}</fans>
			<weibo>{$weibo}</weibo>
		</Photo>
};

(: CODE TO UPDATE - END :)
<weibo_info>  

	
	<Base>
     	{
      		let $baseRoot := $rootNode/DIV[@id="Base"]
      		let $base := local:getInfo($baseRoot)
      		return $base
      	}
      
    </Base>
    <Career>
    	{
    		let $careerRoot := $rootNode/DIV[@id="Career"]
    		let $career := local:getInfo($careerRoot)
    		return $career
    	}
    </Career>
    <Edu>
    	{
    		let $eduRoot := $rootNode/DIV[@id="Edu"]
    		let $edu := local:getInfo($eduRoot)
    		return $edu
    	}
    </Edu>
    <Tag>
    	{
    		let $tagRoot := $rootNode/DIV[@id="Tag"]
    		let $tag := local:getInfo($tagRoot)
    		return $tag
    	}
    </Tag>
    <Verify>
    {
    	let $verify := $rootNode/DIV[@id="Grow"]//DIV[@class="if_verified"]
    	let $tag := local:getGrowInfo($verify)
    	return $tag
    }
    </Verify>
    <Medal>
    	{
    		let $tag := local:getMedalInfo()
    		return $tag
    	}
    </Medal>
    <Level>
    {
    	let $level := $rootNode/DIV[@id="Grow"]//P[@class="level_info"]
    	let $tag := local:getGrowInfo($level)
    	return $tag
    }
    </Level>

		{
			let $main := local:getMain()
			return $main
		}
</weibo_info>