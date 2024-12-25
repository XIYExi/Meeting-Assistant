-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议活动', '2000', '1', 'activity', 'meeting/activity/index', 1, 0, 'C', '0', '0', 'meeting:activity:list', '#', 'admin', sysdate(), '', null, '会议活动菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议活动查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'meeting:activity:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议活动新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'meeting:activity:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议活动修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'meeting:activity:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议活动删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'meeting:activity:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议活动导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'meeting:activity:export',       '#', 'admin', sysdate(), '', null, '');