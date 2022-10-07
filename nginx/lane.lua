local ngx = require "ngx";

local regexHost = [[(.*)\.lane\..*]]
-- $1 为部署名 $2为命名空间
local regexService = [[(.*)\.(.*)\.svc\.cluster\.local]]

-- 参数 "j" 启用 JIT 编译，参数 "o" 是开启缓存必须的
local lane = ngx.re.match(ngx.var.host, regexHost, "jo")
if lane then
    local laneService = ngx.re.match(ngx.var.serviceName, regexService, "jo")
    if lane then
        ngx.req.set_header("laneTag", lane[1])
        ngx.var.serviceName = laneService[1] .. "-lane-" .. lane[1] .. "." .. laneService[2] .. ".svc.cluster.local"
    end
end
-- ngx.log(ngx.ERR, "===============2= serviceName:", ngx.var.serviceName)

-- /usr/local/openresty/nginx/lua/lane.lua