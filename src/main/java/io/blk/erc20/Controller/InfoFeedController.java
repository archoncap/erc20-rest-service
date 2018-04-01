package io.blk.erc20.Controller;


import io.blk.erc20.ContractService;
import io.blk.erc20.Request.ConsumerRequest;
import io.blk.erc20.Request.ContractSpecification;
import io.blk.erc20.Service.InfoFeedService;
import io.blk.erc20.generated.InfoFeed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api("ERC-20 token standard API")
@RestController
@RequestMapping("/infoFeed")
public class InfoFeedController {
    private final io.blk.erc20.Service.InfoFeedService InfoFeedService;

    @Autowired
    public InfoFeedController(InfoFeedService InfoFeedService) {
        this.InfoFeedService = InfoFeedService;
    }

    @ApiOperation(
            value = "Deploy InfoFeed Contract",
            notes = "Returns hex encoded contract address")
    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    String deploy(
            HttpServletRequest request) throws Exception {
        return InfoFeedService.deploy();
    }

    @ApiOperation("Get infofeed testWrite values")
    @RequestMapping(value = "/{contractAddress}/testWrite", method = RequestMethod.GET)
    long testWrite(@PathVariable String contractAddress) throws Exception {
        return InfoFeedService.testWrite(contractAddress);
    }

    @ApiOperation(
            value = "CallInfoFeed Contract",
            notes = "Returns hex encoded contract address")
    @RequestMapping(value = "/{contractAddress}/callContract", method = RequestMethod.GET)
    String callContract(@PathVariable String contractAddress) throws Exception {
        return InfoFeedService.callContract(contractAddress);
    }
}
