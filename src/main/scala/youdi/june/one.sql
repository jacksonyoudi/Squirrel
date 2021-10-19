select 
  IDFA,
  ab_test,
  advertising_id,
  app_version,
  brand,
  client_ip,
  complete_time,
  country as country_s,
  data,
  deviceid,
  event_id,
  event_timestamp,
  log_timestamp,
  model,
  operation,
  os,
  os_version,
  page_section,
  page_type,
  platform,
  platform_implementation,
  sequence_id,
  session_id,
  signature,
  sink_timestamp,
  '0' as sub_sequence_id,
  target_type,
  token,
  type,
  user_agent,
  userid,
  version,
  wifi,
  '' as viewed_object,
  auto_view_id,
  env,
  rn_version,
  domain,
  to_date(sellerdata_udf.get_utc(cast(log_timestamp/1000 as bigint), grass_region)) as dt,
  cast(hour as string) as grass_hour,
  hour(sellerdata_udf.get_utc(cast(log_timestamp/1000 as bigint), grass_region)) as hour,
  grass_region as country,
  operation_type
from 
    shopee.de_tracking_ubt_stream
where 
        grass_date = date('2021-07-20')
    and app_id = 0
    and usage_id = 1
    and grass_region in ('TW', 'SG', 'PH', 'MY', 'ID', 'TH', 'VN', 'BR', 'MX')
    and hour = '1'
    and page_type = 'streaming_room' 