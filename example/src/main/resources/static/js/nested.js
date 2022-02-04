(function($) {
	function generateIdFromInputName(inputName) {
		// See AbstractDataBoundFormElementTag#autogenerateId()
		return inputName.replace(/\[(.*?)\]/g, '$1');
	}

	$(document).on('click', '[data-nested-add]', function(event) {
		const $this = $(this),
			templateId = $this.attr('data-nested-template-id');
			template = document.querySelector('#' + templateId),
			templateContentSelector = $this.attr('data-nested-template-content-selector'),
			templateContent = (templateContentSelector)
				? template.content.querySelectorAll(templateContentSelector)
				: template.content.childNodes;
		let newContent = '';
		for (let i = 0; i < templateContent.length; i++) {
			if (templateContent[i].outerHTML) {
				newContent += templateContent[i].outerHTML;
			}
		}
		const nextIndex = parseInt($this.data('nested-next-index')) || 0;
		const nestedPath = $this.attr('data-nested-add') + '[' + nextIndex + '].';
		const $target = $($this.attr('data-nested-insert'));
		if ($target.length === 0) {
			console.warn('Could not find the element to insert the template.'
					+ ' Make sure your `data-nested-insert-*` is correct.');
		}
		else {
			$this.data('nested-next-index', nextIndex + 1);
			const insertMethod = $this.attr('data-nested-insert-method') || 'before'; // or after
			$target[insertMethod]($(newContent
					.replace(/name\s*=\s*"(.*?)"/g,
							'name="' + nestedPath + '$1"')
					.replace(/(id|for)\s*=\s*"(.*?)"/g,
							'$1="' + generateIdFromInputName(nestedPath) + '$2"')));
		}
	});

	$(document).on('click', '[data-nested-remove]', function(event) {
		const $this = $(this),
			selector = $this.attr('data-nested-remove') || '[data-nested-fields]',
			$nodeToDelete = $this.closest(selector);
		if ($nodeToDelete.length == 0) {
			console.warn('Could not find the element to remove.'
					+ ' Make sure you have an element with `data-nested-fields` attribute.');
		}
		else {
			setTimeout(function() {
				$nodeToDelete.detach();
			}, 0);
		}
	});

	function extractIndexedSubpaths(path) {
		const subpaths = [];
		let position = 0, indexOfClosingBracket;
		while (position < path.length) {
			if ((indexOfClosingBracket = path.indexOf(']', position)) != -1) {
				subpaths.push(path.substring(0, indexOfClosingBracket + 1));
				position = indexOfClosingBracket + 1;
				continue;
			}
			break;
		}
		return subpaths;
	}

	function renumberIndexedPaths($inputs, mappings = {}, counters = {}) {
		$inputs.each((index, input) => {
			const subpaths = extractIndexedSubpaths(input.name);
			subpaths.forEach((subpath) => {
				// Initialize counters and mappings
				if (!mappings.hasOwnProperty(subpath)) {
					const subpathMinusIndex = subpath.substring(0, subpath.lastIndexOf('['));
					const newIndex = counters[subpathMinusIndex] || 0;
					mappings[subpath] = subpathMinusIndex + '[' + newIndex + ']';
					counters[subpathMinusIndex] = newIndex + 1;
				}
			});
			if (subpaths.length > 0) {
				const newName = input.name.replace(
						subpaths[subpaths.length - 1],
						mappings[subpaths[subpaths.length - 1]])
				if (input.name != newName) {
					const newId = generateIdFromInputName(newName);
					// console.log(`${input.name} => ${newName}`);
					input.name = newName;
					$(`label[for="${input.id}"]`).each((index, label) => {
						label.htmlFor = newId;
					});
					input.id = newId;
				}
			}
		});
	}

	$(document).on('submit', 'form[data-nested-reindex]', function(event) {
		const $this = $(this),
			$inputs = $this.find(':input[name]');
		renumberIndexedPaths($inputs);
	});
})(jQuery);
