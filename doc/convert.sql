-- 选取直播广告的连接到推送表
create table addata as 	select a.ad_name , a.made_id,a.maybe_id ,a.begin_time,a.end_time,a.ad_type_id,a.ad_type_name,a.ad_type_pid,a.ad_type_pname,a.illegal_content,a.dura,a.illegal_code,
       b.live_start_time,b.live_end_time,b.live_audience_count,b.live_video_path,b.site_id,b.media_name,b.goods_count,b.watch_count,b.live_dura_time,b.live_room_owner_name,b.live_user_head_portraits,
       c.user_id,
       d.area_name
		from im_made a
		left join im_maybe_ad b on a.maybe_id = b.maybe_id
		left join crawl_user_info c on b.live_user_id = c.user_id
        left join pub_area d on d.area_code = c.area_code

drop table addata

create table im_maybe_ad_backup as select * from im_maybe_ad

select a.*,b.made_id,b.made_id from im_maybe_ad a left join im_made b on a.maybe_id = b.maybe_id

select * from