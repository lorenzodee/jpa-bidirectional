/*
 * Copyright 2021-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.lorenzodee.example;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
class OrdersController {

	@Autowired
	Orders allOrders;

	@Autowired
	MessageSource messageSource;

	// If direct field access is to be the default, move this to a @ControllerAdvice
	@InitBinder
	void initBinder(WebDataBinder binder) {
		binder.initDirectFieldAccess();
	}

	@GetMapping
	String index(Pageable pageable, Model model) {
		// Or, use this.allOrders.queryAll(pageable) for a projection
		model.addAttribute("ordersPage", this.allOrders.findAll(pageable));
		// In the view, use ${ordersPage.content} to get List<Order>
		return "orders/index";
	}

	@GetMapping("/{id}")
	String show(@PathVariable Long id, Model model) {
		// Or, use this.allOrders.queryById(id) for a projection
		model.addAttribute("order",
				this.allOrders.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
		return "orders/show";
	}

	@GetMapping("/create")
	String create(Model model) {
		model.addAttribute("order", new Order());
		// Also provide models to support the <form> (e.g. drop lists)
		prepareFormModels(model);
		return "orders/create";
	}

	@PostMapping
	String save(@Valid Order order, BindingResult bindingResult,
			Model model, RedirectAttributes redirectAttrs, Locale locale) {
		if (bindingResult.hasErrors()) {
			// Also provide models to support the <form> (e.g. drop lists)
			prepareFormModels(model);
			return "orders/create";
		}

		// order.setId(null);
		order = this.allOrders.save(order);
		// Or, use application-layer @Transactional @Service to create order

		// @formatter:off
		/*
		redirectAttrs.addFlashAttribute("message",
				this.messageSource.getMessage("order.created",
						new Object[] { order.getId() }, locale));
		*/
		// @formatter:on
		return "redirect:/orders";
	}

	@GetMapping("/{id:\\d+}/edit")
	String edit(@PathVariable Long id, Model model) {
		model.addAttribute("order",
				this.allOrders.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
		// Also provide models to support the <form> (e.g. drop lists)
		prepareFormModels(model);
		return "orders/edit";
	}

	@PutMapping("/{id:\\d+}")
	String update(@Valid Order order, BindingResult bindingResult,
			Model model, RedirectAttributes redirectAttrs, Locale locale) {
		if (bindingResult.hasErrors()) {
			// Also provide models to support the <form> (e.g. drop lists)
			prepareFormModels(model);
			return "orders/edit";
		}
		// Spring MVC will bind the "id" path variable to order.id.
		// So, there's no need to explicitly do order.setId(...).

		order = this.allOrders.save(order);
		// Or, use application-layer @Transactional @Service to create order

		// @formatter:off
		/*
		redirectAttrs.addFlashAttribute("message",
				this.messageSource.getMessage("order.updated",
						new Object[] { order.getId() }, locale));
		*/
		// @formatter:on
		return "redirect:/orders";
	}

	void prepareFormModels(Model model) {
		// Provide models to support the create/edit <form> (e.g. drop lists)
	}

	@DeleteMapping("/{id:\\d+}")
	String delete(@PathVariable Long id, Model model) {
		try {
			this.allOrders.deleteById(id);
			return "redirect:/orders";
		}
		catch (EmptyResultDataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
