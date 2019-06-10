import '../FieldBase/FieldBase.es';
import './EditorRegister.soy.js';
import Component from 'metal-component';
import Soy from 'metal-soy';
import templates from './Editor.soy.js';
import {Config} from 'metal-state';

class Editor extends Component {
	attached() {
		this._createEditor();
	}

	created() {
		AUI().use('liferay-alloy-editor', A => {
			this.A = A;
		});
	}

	shouldUpdate() {
		return false;
	}

	syncValue(value) {
		const {_alloyEditor} = this;

		if (_alloyEditor && _alloyEditor.getHTML() !== value) {
			const nativeEditor = _alloyEditor.getNativeEditor();
			const {hasFocus} = nativeEditor.focusManager;

			if (!hasFocus) {
				nativeEditor.setData(value);
			}
		}
	}

	_createEditor() {
		const {A, name, readOnly, value} = this;
		const editorNode = this.element.querySelector('.alloy-editor');

		if (readOnly) {
			return;
		}

		editorNode.innerHTML = value;

		window[name] = {};

		this._alloyEditor = new A.LiferayAlloyEditor({
			contents: value,
			editorConfig: {
				extraPlugins: 'ae_placeholder,ae_selectionregion,ae_uicore',
				removePlugins:
					'contextmenu,elementspath,image,link,liststyle,resize,tabletools,toolbar',
				spritemap: `${themeDisplay.getPathThemeImages()}/lexicon/icons.svg`,
				srcNode: A.one(editorNode),
				toolbars: {
					add: {
						buttons: ['hline', 'table']
					},
					styles: {
						selections: AlloyEditor.Selections,
						tabIndex: 1
					}
				}
			},
			namespace: name,
			onChangeMethod: this._onChangeEditor.bind(this),
			plugins: [],
			textMode: false
		}).render();

		this._alloyEditor
			.getNativeEditor()
			.on('actionPerformed', this._onActionPerformed.bind(this));
	}

	_onActionPerformed(event) {
		const {
			data: {props}
		} = event;

		if (!props.command) {
			this._onChangeEditor(event);
		}
	}

	_onChangeEditor(event) {
		this.emit('fieldEdited', {
			fieldInstance: this,
			originalEvent: event,
			value: this._alloyEditor.getHTML()
		});
	}
}

Editor.STATE = {
	/**
	 * @default false
	 * @instance
	 * @memberof Editor
	 * @type {?bool}
	 */

	evaluable: Config.bool().value(false),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Editor
	 * @type {?(string|undefined)}
	 */

	fieldName: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Editor
	 * @type {?(string|undefined)}
	 */

	id: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Editor
	 * @type {?(string|undefined)}
	 */

	label: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Editor
	 * @type {?(string|undefined)}
	 */

	name: Config.string().required(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Editor
	 * @type {?(string|undefined)}
	 */

	placeholder: Config.string(),

	/**
	 * @default false
	 * @instance
	 * @memberof Editor
	 * @type {?bool}
	 */

	readOnly: Config.bool().value(false),

	/**
	 * @default undefined
	 * @instance
	 * @memberof FieldBase
	 * @type {?(bool|undefined)}
	 */

	repeatable: Config.bool(),

	/**
	 * @default false
	 * @instance
	 * @memberof Editor
	 * @type {?(bool|undefined)}
	 */

	required: Config.bool().value(false),

	/**
	 * @default true
	 * @instance
	 * @memberof Editor
	 * @type {?(bool|undefined)}
	 */

	showLabel: Config.bool().value(true),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Editor
	 * @type {?(string|undefined)}
	 */

	spritemap: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Editor
	 * @type {?(string|undefined)}
	 */

	tip: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof FieldBase
	 * @type {?(string|undefined)}
	 */

	tooltip: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Text
	 * @type {?(string|undefined)}
	 */

	type: Config.string().value('editor'),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Editor
	 * @type {?(string|undefined)}
	 */

	value: Config.string().value('')
};

Soy.register(Editor, templates);

export default Editor;
