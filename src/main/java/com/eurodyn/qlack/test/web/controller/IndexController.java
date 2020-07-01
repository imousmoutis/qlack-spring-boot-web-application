package com.eurodyn.qlack.test.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ddddd
 */
@Controller
@RequestMapping(value = {"/api/substance"})
public class IndexController {

  /**
   * Test javadoc
   * @return tt
   */
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String homepage() {
    return "index";
  }

}
