-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议', '2000', '1', 'meeting', 'meeting/meeting/index', 1, 0, 'C', '0', '0', 'meeting:meeting:list', '#', 'admin', sysdate(), '', null, '会议菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'meeting:meeting:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'meeting:meeting:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'meeting:meeting:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'meeting:meeting:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'meeting:meeting:export',       '#', 'admin', sysdate(), '', null, '');