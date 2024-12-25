-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议活动板块', '2000', '1', 'sector', 'meeting/sector/index', 1, 0, 'C', '0', '0', 'meeting:sector:list', '#', 'admin', sysdate(), '', null, '会议活动板块菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议活动板块查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'meeting:sector:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议活动板块新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'meeting:sector:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议活动板块修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'meeting:sector:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议活动板块删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'meeting:sector:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议活动板块导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'meeting:sector:export',       '#', 'admin', sysdate(), '', null, '');