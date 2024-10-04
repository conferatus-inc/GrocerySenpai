package org.example.mainbackend.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(value = "python", url = "https://localhost:8081/")
interface PythonClient {
    @RequestMapping(method = [RequestMethod.GET], value = ["/data"])
    fun getData(): List<Data?>?

    @RequestMapping(method = [RequestMethod.GET], value = ["/data/{dataId}"], produces = ["application/json"])
    fun getDataById(
        @PathVariable("dataId") postId: Long?,
    ): Data?
}
