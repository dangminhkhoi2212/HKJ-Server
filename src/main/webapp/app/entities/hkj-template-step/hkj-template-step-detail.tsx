import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-template-step.reducer';

export const HkjTemplateStepDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjTemplateStepEntity = useAppSelector(state => state.hkjTemplateStep.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjTemplateStepDetailsHeading">
          <Translate contentKey="serverApp.hkjTemplateStep.detail.title">HkjTemplateStep</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjTemplateStepEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="serverApp.hkjTemplateStep.name">Name</Translate>
            </span>
          </dt>
          <dd>{hkjTemplateStepEntity.name}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjTemplateStep.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjTemplateStepEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjTemplateStep.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjTemplateStepEntity.createdDate ? (
              <TextFormat value={hkjTemplateStepEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjTemplateStep.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjTemplateStepEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjTemplateStep.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjTemplateStepEntity.lastModifiedDate ? (
              <TextFormat value={hkjTemplateStepEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjTemplateStep.hkjTemplate">Hkj Template</Translate>
          </dt>
          <dd>{hkjTemplateStepEntity.hkjTemplate ? hkjTemplateStepEntity.hkjTemplate.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-template-step" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-template-step/${hkjTemplateStepEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjTemplateStepDetail;
